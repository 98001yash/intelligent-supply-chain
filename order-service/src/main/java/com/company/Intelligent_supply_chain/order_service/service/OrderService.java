package com.company.Intelligent_supply_chain.order_service.service;

import com.company.Intelligent_supply_chain.order_service.dtos.OrderDto;
import com.company.Intelligent_supply_chain.order_service.entities.Order;
import com.company.Intelligent_supply_chain.order_service.enums.OrderStatus;
import com.company.Intelligent_supply_chain.order_service.exceptions.ResourceNotFoundException;
import com.company.Intelligent_supply_chain.order_service.kafka.OrderEventProducer;
import com.company.Intelligent_supply_chain.order_service.repository.OrderRepository;

import com.company.intelligent_supply_chain.events.BaseEvent;
import com.company.intelligent_supply_chain.events.OrderCreatedEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    private final OrderEventProducer orderEventProducer;


    @Transactional
    public OrderDto placeOrder(OrderDto orderDto) {

        log.info("Creating order for customer ID: {}",
                orderDto.getCustomerId());

        // Convert DTO -> Entity
        Order order = modelMapper.map(orderDto, Order.class);

        // Initial async state
        order.setOrderStatus(OrderStatus.CREATED);

        // Save Order
        Order savedOrder = orderRepository.save(order);

        log.info("Order saved successfully with ID: {}",
                savedOrder.getId());

        // Create Kafka Event
        OrderCreatedEvent event =
                OrderCreatedEvent.builder()

                        .eventId(BaseEvent.generateEventId())

                        .eventType("ORDER_CREATED")

                        .correlationId(
                                savedOrder.getId().toString()
                        )

                        .timestamp(LocalDateTime.now())
                        .orderId(savedOrder.getId())
                        .customerId(savedOrder.getCustomerId())
                        .skuCode(Long.valueOf(savedOrder.getSkuCode()))
                        .quantity(savedOrder.getQuantity())
                        .totalPrice(savedOrder.getTotalPrice())
                        .build();

        // Publish Event
        orderEventProducer.publishOrderCreatedEvent(event);

        log.info("OrderCreatedEvent published for Order ID: {}",
                savedOrder.getId());

        return modelMapper.map(savedOrder, OrderDto.class);
    }


    public List<OrderDto> getOrdersByCustomer(Long customerId) {

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(order ->
                        modelMapper.map(order, OrderDto.class)
                )
                .collect(Collectors.toList());
    }


    public OrderDto getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + orderId
                        ));

        return modelMapper.map(order, OrderDto.class);
    }


    public void updateOrderStatus(
            Long orderId,
            String status
    ) {

        log.info("Updating order status for Order ID: {}",
                orderId);

        Order order = orderRepository.findById(orderId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + orderId
                        ));

        order.setOrderStatus(
                OrderStatus.valueOf(status.toUpperCase())
        );

        orderRepository.save(order);

        log.info("Order status updated successfully");
    }


    @Transactional
    public void deleteOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + orderId
                        ));

        orderRepository.delete(order);
        log.info("Order deleted successfully");
    }


    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + orderId
                        ));

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Order cancelled successfully");
    }
}