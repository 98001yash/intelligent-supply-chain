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
        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        PaymentDto paymentDto = PaymentDto.builder()
                .orderId(savedOrder.getId())
                .amount(orderDto.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .build();

        log.info("Initiating payment for Order ID: {}", savedOrder.getId());
        PaymentDto processedPayment = paymentClient.processPayment(paymentDto);
        log.info("Payment processed with status: {}", processedPayment.getStatus());

        if (processedPayment.getStatus() == PaymentStatus.SUCCESS) {
            savedOrder.setOrderStatus(OrderStatus.CONFIRMED);

            log.info("Updating stock for SKU: {}", orderDto.getSkuCode());
            try {
                inventoryClient.updateStock(new InventoryUpdateRequest(orderDto.getSkuCode(), orderDto.getQuantity()));
            } catch (Exception e) {
                log.error("Failed to update stock for SKU: {}", orderDto.getSkuCode(), e);
                throw new RuntimeException("Stock update failed");
            }
        } else {
            savedOrder.setOrderStatus(OrderStatus.CANCELLED);
            log.warn("Order ID {} was cancelled due to failed payment", savedOrder.getId());
        }

        orderRepository.save(savedOrder);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    public List<OrderDto> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setOrderStatus(status);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
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


    public void cancelOrder(Long orderId){
        Order order  =orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));

        if(order.getOrderStatus()==OrderStatus.CONFIRMED){
            throw new RuntimeException("Cannot cancel a confirmed order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


}
