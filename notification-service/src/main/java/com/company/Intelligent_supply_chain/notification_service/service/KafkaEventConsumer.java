package com.company.Intelligent_supply_chain.notification_service.service;

import com.company.Intelligent_supply_chain.notification_service.dtos.LowStockAlert;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventConsumer {

    @KafkaListener(topics = "low-stock-alerts", groupId = "notification-group")
    public void handleLowStockAlert(LowStockAlert lowStockAlert) {
        // Handle the low stock alert
        System.out.println("Received low stock alert: " + lowStockAlert);
        System.out.println("SKU Code: " + lowStockAlert.getSkuCode());
        System.out.println("Remaining Quantity: " + lowStockAlert.getQuantity());
    }
}