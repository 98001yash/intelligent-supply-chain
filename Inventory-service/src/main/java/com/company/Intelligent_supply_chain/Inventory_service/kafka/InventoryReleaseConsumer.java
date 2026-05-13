package com.company.Intelligent_supply_chain.Inventory_service.kafka;


import com.company.Intelligent_supply_chain.Inventory_service.service.InventoryService;
import com.company.intelligent_supply_chain.events.InventoryReleasedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryReleaseConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "inventory-release-topic",
            groupId = "inventory-release-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeInventoryReleasedEvent(
            InventoryReleasedEvent event
    ) {

        log.info(
                "Received InventoryReleasedEvent for Order ID: {}",
                event.getOrderId()
        );
        inventoryService.releaseStock(
                event.getSkuCode(),
                event.getQuantity()
        );
        log.info(
                "Inventory restored successfully for SKU: {}",
                event.getSkuCode()
        );
    }
}
