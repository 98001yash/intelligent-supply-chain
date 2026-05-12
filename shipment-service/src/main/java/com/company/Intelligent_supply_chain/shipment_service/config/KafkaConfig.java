package com.company.Intelligent_supply_chain.shipment_service.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic paymentProcessedTopic() {
        return TopicBuilder.name("payment-processed-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic shipmentCreatedTopic() {
        return TopicBuilder.name("shipment-created-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic shipmentCancelledTopic() {
        return TopicBuilder.name("shipment-cancelled-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}