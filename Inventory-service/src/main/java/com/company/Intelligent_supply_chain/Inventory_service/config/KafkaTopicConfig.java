package com.company.Intelligent_supply_chain.Inventory_service.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic lowStockAlertsTopic() {

        return new NewTopic(
                "low-stock-alerts",
                3,
                (short) 1
        );
    }


    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic(
                "order-created-topic",
                3,
                (short) 1
        );
    }


    @Bean
    public NewTopic inventoryReservedTopic() {
        return new NewTopic(
                "inventory-reserved-topic",
                3,
                (short) 1
        );
    }


    @Bean
    public ProducerFactory<String, Object>
    producerFactory() {

        Map<String, Object> config =
                new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(
                config
        );
    }


    @Bean
    public KafkaTemplate<String, Object>
    kafkaTemplate() {

        return new KafkaTemplate<>(
                producerFactory()
        );
    }
}