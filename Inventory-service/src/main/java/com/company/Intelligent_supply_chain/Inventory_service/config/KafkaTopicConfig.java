package com.company.Intelligent_supply_chain.Inventory_service.config;

import com.company.Intelligent_supply_chain.Inventory_service.dtos.LowStockAlert;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    // Create a new Kafka topic named "low-stock-alerts"
    @Bean
    public NewTopic lowStockAlertsTopic() {
        return new NewTopic("low-stock-alerts", 3, (short) 1);
    }

    // KafkaTemplate for sending LowStockAlert messages
    @Bean
    public KafkaTemplate<String, LowStockAlert> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // ProducerFactory for creating Kafka producers
    @Bean
    public ProducerFactory<String, LowStockAlert> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
}