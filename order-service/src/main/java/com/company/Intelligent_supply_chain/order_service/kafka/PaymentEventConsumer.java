package com.company.Intelligent_supply_chain.order_service.kafka;


import com.company.Intelligent_supply_chain.order_service.entities.Order;
import com.company.Intelligent_supply_chain.order_service.enums.OrderStatus;
import com.company.Intelligent_supply_chain.order_service.repository.OrderRepository;
import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "payment-processed-topic",
            groupId = "order-payment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentProcessedEvent(
            PaymentProcessedEvent event
    ) {

        log.info(
                "Received PaymentProcessedEvent for Order ID: {}",
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

        if ("SUCCESS".equalsIgnoreCase(
                event.getPaymentStatus()
        )) {

            order.setOrderStatus(
                    OrderStatus.CONFIRMED
            );

            log.info(
                    "Order CONFIRMED for Order ID: {}",
                    event.getOrderId()
            );

        } else {

            order.setOrderStatus(
                    OrderStatus.CANCELLED
            );

            log.info(
                    "Order CANCELLED due to payment failure for Order ID: {}",
                    event.getOrderId()
            );
        }

        orderRepository.save(order);

        log.info(
                "Order status updated successfully for Order ID: {}",
                event.getOrderId()
        );
    }
}