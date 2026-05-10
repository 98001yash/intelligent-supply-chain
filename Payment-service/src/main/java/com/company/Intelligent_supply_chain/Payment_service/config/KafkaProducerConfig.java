package com.company.Intelligent_supply_chain.Payment_service.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic paymentProcessedTopic() {
        return new NewTopic(
                "payment-processed-topic",
                3,
                (short) 1
        );
    }
}