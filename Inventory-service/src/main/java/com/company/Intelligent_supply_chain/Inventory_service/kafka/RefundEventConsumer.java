package com.company.Intelligent_supply_chain.Inventory_service.kafka;


import com.company.Intelligent_supply_chain.Inventory_service.service.InventoryService;

import com.company.intelligent_supply_chain.events.BaseEvent;
import com.company.intelligent_supply_chain.events.InventoryRestoredEvent;
import com.company.intelligent_supply_chain.events.RefundProcessedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundEventConsumer {

    private final InventoryService inventoryService;

    private final InventoryEventProducer inventoryEventProducer;

    @KafkaListener(
            topics = "refund-processed-topic",
            groupId = "inventory-refund-group",
            containerFactory =
                    "refundKafkaListenerContainerFactory"
    )
    public void consumeRefundProcessedEvent(
            RefundProcessedEvent event
    ) {

        log.info(
                "Received RefundProcessedEvent for Order ID: {}",
                event.getOrderId()
        );

        // ONLY restore if refund successful

        if (!"SUCCESS".equalsIgnoreCase(
                event.getRefundStatus()
        )) {

            log.warn(
                    "Refund failed for Order ID: {}. Inventory restoration skipped.",
                    event.getOrderId()
            );

            return;
        }

        // TEMPORARY STATIC VALUES
        // Later we’ll dynamically map SKU + quantity

        String skuCode = "IPHONE_15";
        Integer quantity = 1;

        inventoryService.releaseStock(
                skuCode,
                quantity
        );

        log.info(
                "Inventory restored for SKU: {}",
                skuCode
        );

        InventoryRestoredEvent restoredEvent =
                InventoryRestoredEvent.builder()

                        .eventId(
                                BaseEvent.generateEventId()
                        )
                        .eventType(
                                "INVENTORY_RESTORED"
                        )
                        .correlationId(
                                event.getCorrelationId()
                        )
                        .timestamp(
                                LocalDateTime.now()
                        )
                        .orderId(
                                event.getOrderId()
                        )
                        .skuCode(
                                skuCode
                        )
                        .restoredQuantity(
                                quantity
                        )
                        .restoreStatus(
                                "SUCCESS"
                        )
                        .build();
        inventoryEventProducer
                .publishInventoryRestoredEvent(
                        restoredEvent
                );

        log.info(
                "InventoryRestoredEvent published for Order ID: {}",
                event.getOrderId()
        );
    }
}
