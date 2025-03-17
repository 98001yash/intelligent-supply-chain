package com.company.Intelligent_supply_chain.Inventory_service.service;


import com.company.Intelligent_supply_chain.Inventory_service.entities.Inventory;
import com.company.Intelligent_supply_chain.Inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void checkAndPublishLowStockAlert(String skuCode){
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(()->new RuntimeException("Inventory not found for SKU: "+skuCode));

        if(inventory.getQuantity()<5){ // low stock threshold
            String message = "low stock alert for SKU: " + skuCode + ". Only " + inventory.getQuantity() + "left!";
            kafkaTemplate.send("low-stock-alerts",message);
        }
    }
}
