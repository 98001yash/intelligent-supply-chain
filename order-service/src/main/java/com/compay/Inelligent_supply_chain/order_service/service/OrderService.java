package com.compay.Inelligent_supply_chain.order_service.service;

import com.compay.Inelligent_supply_chain.order_service.clients.InventoryClient;
import com.compay.Inelligent_supply_chain.order_service.clients.PaymentClient;
import com.compay.Inelligent_supply_chain.order_service.dtos.InventoryUpdateRequest;
import com.compay.Inelligent_supply_chain.order_service.dtos.OrderDto;
import com.compay.Inelligent_supply_chain.order_service.dtos.PaymentDto;
import com.compay.Inelligent_supply_chain.order_service.entities.Order;
import com.compay.Inelligent_supply_chain.order_service.enums.OrderStatus;
import com.compay.Inelligent_supply_chain.order_service.enums.PaymentStatus;
import com.compay.Inelligent_supply_chain.order_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;

    @Transactional
    public OrderDto placeOrder(OrderDto orderDto) {
        log.info("Checking stock availability for SKU: {}", orderDto.getSkuCode());
        boolean isAvailable = inventoryClient.checkStock(orderDto.getSkuCode());

        if (!isAvailable) {
            throw new RuntimeException("Product is out of stock");
        }

        Order order = modelMapper.map(orderDto, Order.class);
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        log.info("✅ Order placed successfully with ID: {}", savedOrder.getId());

        // Initiate Payment
        PaymentDto paymentDto = PaymentDto.builder()
                .orderId(savedOrder.getId())
                .amount(orderDto.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .build();

        log.info("💰 Initiating payment for Order ID: {}", savedOrder.getId());
        paymentClient.processPayment(paymentDto);

        // 🔄 Retry to get final payment status
        PaymentDto finalPayment = getFinalPaymentStatus(savedOrder.getId());
        log.info("📌 Final Payment Status for Order ID {}: {}", savedOrder.getId(), finalPayment.getStatus());

        if (finalPayment.getStatus() == PaymentStatus.SUCCESS) {
            savedOrder.setOrderStatus(OrderStatus.CONFIRMED);

            log.info("🛒 Updating stock for SKU: {}", orderDto.getSkuCode());
            try {
                inventoryClient.updateStock(new InventoryUpdateRequest(orderDto.getSkuCode(), orderDto.getQuantity()));
            } catch (Exception e) {
                log.error("❌ Failed to update stock for SKU: {}", orderDto.getSkuCode(), e);
                throw new RuntimeException("Stock update failed");
            }
        }
        // ✅ Instead of cancelling, mark it as "PROCESSING_PAYMENT"
        else if (finalPayment.getStatus() == PaymentStatus.PENDING) {
            savedOrder.setOrderStatus(OrderStatus.PROCESSING_PAYMENT);
            log.warn("⌛ Order ID {} is awaiting payment confirmation", savedOrder.getId());
        }
        else {
            savedOrder.setOrderStatus(OrderStatus.CANCELLED);
            log.warn("❌ Order ID {} was cancelled due to failed payment", savedOrder.getId());
        }

        orderRepository.save(savedOrder);
        return modelMapper.map(savedOrder, OrderDto.class);
    }


    private PaymentDto getFinalPaymentStatus(Long orderId) {
        PaymentDto paymentDto = null;
        int retryCount = 3;

        for (int i = 0; i < retryCount; i++) {
            try {
                Thread.sleep(2000);  // Wait for 2 seconds before retrying
                paymentDto = paymentClient.getPaymentByOrderId(orderId);

                if (paymentDto.getStatus() != PaymentStatus.PENDING) {
                    return paymentDto; // ✅ Return if status is SUCCESS or FAILED
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Error while waiting for payment status", e);
            }
        }

        return paymentDto; // Might still be PENDING after retries
    }


    public List<OrderDto> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(Long orderId, PaymentStatus status) {
        log.info("📢 Received payment update for Order ID: {}. Updating order status...", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for ID: " + orderId));

        // ✅ Convert PaymentStatus → OrderStatus
        if (status == PaymentStatus.SUCCESS) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
            log.info("✅ Order ID: {} is now CONFIRMED", orderId);
        } else if (status == PaymentStatus.FAILED) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            log.warn("❌ Order ID: {} is CANCELLED due to failed payment", orderId);
        } else {
            order.setOrderStatus(OrderStatus.PENDING);
            log.warn("⌛ Order ID: {} is still PENDING", orderId);
        }

        orderRepository.save(order);
        log.info("🔄 Order status updated in DB for Order ID: {}", orderId);
    }


    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        orderRepository.delete(order);
    }

    public OrderDto getOrderById(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with ID: "+orderId));
        return modelMapper.map(order,OrderDto.class);
    }


    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            // Restore inventory
            inventoryClient.updateStock(new InventoryUpdateRequest(order.getSkuCode(), order.getQuantity()));

            // Trigger a refund
            PaymentDto refundRequest = PaymentDto.builder()
                    .orderId(orderId)
                    .amount(order.getTotalPrice())
                    .status(PaymentStatus.REFUNDED)
                    .build();

            paymentClient.processPayment(refundRequest);
        }

        // Mark the order as cancelled
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order ID {} cancelled successfully", orderId);
    }


    public void handlePaymentUpdate(PaymentDto paymentDto){
        log.info("🔄 Handling payment update for Order ID: {}", paymentDto.getOrderId());

        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(paymentDto.getStatus() == PaymentStatus.SUCCESS){
            order.setOrderStatus(OrderStatus.CONFIRMED);
            log.info("✅ Order ID {} confirmed after successful payment", order.getId());
        } else if(paymentDto.getStatus() == PaymentStatus.FAILED){
            order.setOrderStatus(OrderStatus.CANCELLED);
            log.warn("❌ Order ID {} cancelled due to payment failure", order.getId());
        }

        orderRepository.save(order);
    }


    @Scheduled(fixedDelay = 60000) // Runs every 60 seconds
    public void checkPendingPayments() {
        List<Order> pendingOrders = orderRepository.findByOrderStatus(OrderStatus.PROCESSING_PAYMENT);
        for (Order order : pendingOrders) {
            PaymentDto paymentStatus = paymentClient.getPaymentStatus(order.getId());

            if (PaymentStatus.SUCCESS.equals(paymentStatus.getStatus())) {
                order.setOrderStatus(OrderStatus.CONFIRMED);
                log.info("✅ Order ID {} confirmed after successful payment", order.getId());
                orderRepository.save(order);
            }
            else if (PaymentStatus.FAILED.equals(paymentStatus.getStatus())) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                log.warn("❌ Order ID {} cancelled due to payment failure", order.getId());
                orderRepository.save(order);
            }
        }
    }


}

