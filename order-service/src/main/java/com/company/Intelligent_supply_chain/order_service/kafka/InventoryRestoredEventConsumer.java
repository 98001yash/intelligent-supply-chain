package com.company.Intelligent_supply_chain.order_service.kafka;

import com.company.Intelligent_supply_chain.order_service.entities.Order;
import com.company.Intelligent_supply_chain.order_service.enums.OrderStatus;
import com.company.Intelligent_supply_chain.order_service.repository.OrderRepository;
import com.company.intelligent_supply_chain.events.InventoryRestoredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryRestoredEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "inventory-restored-topic",
            groupId = "order-return-group",
            containerFactory =
                    "inventoryRestoredKafkaListenerContainerFactory"
    )
    public void consumeInventoryRestoredEvent(
            InventoryRestoredEvent event
    ) {

        log.info(
                "Received InventoryRestoredEvent for Order ID: {}",
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
                OrderStatus.RETURN_COMPLETED
        );

        orderRepository.save(order);
        log.info(
                "Order marked as RETURN_COMPLETED for Order ID: {}",
                event.getOrderId()
        );
    }
}