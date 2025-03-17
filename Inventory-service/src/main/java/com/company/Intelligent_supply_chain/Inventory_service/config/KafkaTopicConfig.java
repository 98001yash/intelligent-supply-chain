package com.company.Intelligent_supply_chain.Inventory_service.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic lowStockAlertsTopic(){
        return new NewTopic("low-stock-alerts",3,(short) 1);
    }
}
