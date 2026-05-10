package com.company.Intelligent_supply_chain.Inventory_service.kafka;



import com.company.Intelligent_supply_chain.Inventory_service.service.InventoryService;
import com.company.intelligent_supply_chain.events.BaseEvent;
import com.company.intelligent_supply_chain.events.InventoryReservedEvent;
import com.company.intelligent_supply_chain.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;


    @KafkaListener(
            topics = "order-created-topic",
            groupId = "inventory-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderCreatedEvent(
            OrderCreatedEvent event
    ) {

        log.info(
                "Received OrderCreatedEvent for Order ID: {}",
                event.getOrderId()
        );

        boolean reserved = inventoryService.reserveStock(
                String.valueOf(event.getSkuCode()),
                event.getQuantity()
        );

        InventoryReservedEvent reservedEvent =
                InventoryReservedEvent.builder()
                        .eventId(BaseEvent.generateEventId())
                        .eventType("INVENTORY_RESERVED")

                        .correlationId(
                                event.getCorrelationId()
                        )
                        .timestamp(LocalDateTime.now())
                        .orderId(event.getOrderId())
                        .skuCode(event.getSkuCode())
                        .quantity(event.getQuantity())
                        .reserved(reserved)
                        .build();

        inventoryEventProducer
                .publishInventoryReservedEvent(
                        reservedEvent
                );

        log.info(
                "InventoryReservedEvent published for Order ID: {}",
                event.getOrderId()
        );
    }
}