package com.company.Intelligent_supply_chain.Inventory_service.service;


import com.company.Intelligent_supply_chain.Inventory_service.dtos.LowStockAlert;
import com.company.Intelligent_supply_chain.Inventory_service.entities.Inventory;
import com.company.Intelligent_supply_chain.Inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventProducer {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, LowStockAlert> kafkaTemplate;

    public void checkAndPublishLowStockAlert(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Inventory not found for SKU: " + skuCode));

        if (inventory.getQuantity() < 5) { // Low stock threshold
            LowStockAlert alert = new LowStockAlert(skuCode, inventory.getQuantity());

            log.info("🔴 Sending Kafka Message - Key: {}, Payload: {}", skuCode, alert);

            // Sending message with explicit key
            kafkaTemplate.send("low-stock-alerts", skuCode, alert)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("❌ Kafka Send Failed", ex);
                        } else {
                            log.info("✅ Kafka Message Sent - Key: {}, Partition: {}", skuCode, result.getRecordMetadata().partition());
                        }
                    });
        }
    }
}