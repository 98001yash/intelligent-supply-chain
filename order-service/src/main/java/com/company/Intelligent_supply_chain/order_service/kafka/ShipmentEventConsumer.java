package com.company.Intelligent_supply_chain.order_service.kafka;

import com.company.Intelligent_supply_chain.order_service.entities.Order;
import com.company.Intelligent_supply_chain.order_service.enums.OrderStatus;
import com.company.Intelligent_supply_chain.order_service.repository.OrderRepository;
import com.company.intelligent_supply_chain.events.ShipmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "shipment-created-topic",
            groupId = "order-shipment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeShipmentCreatedEvent(
            ShipmentCreatedEvent event
    ) {

        log.info(
                "Received ShipmentCreatedEvent for Order ID: {}",
                event.getOrderId()
        );

        Order order =
                orderRepository.findById(
                        event.getOrderId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with ID: "
                                        + event.getOrderId()
                        )
                );

        order.setOrderStatus(
                OrderStatus.SHIPPED
        );

        orderRepository.save(order);

        log.info(
                "Order marked as SHIPPED for Order ID: {}",
                event.getOrderId()
        );
    }
}