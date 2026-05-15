package com.company.Intelligent_supply_chain.order_service.config;

import com.company.intelligent_supply_chain.events.InventoryRestoredEvent;
import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;
import com.company.intelligent_supply_chain.events.ShipmentCreatedEvent;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, PaymentProcessedEvent>
    paymentConsumerFactory() {

        JsonDeserializer<PaymentProcessedEvent>
                deserializer =
                new JsonDeserializer<>(
                        PaymentProcessedEvent.class
                );

        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        Map<String, Object> config =
                new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "order-payment-group"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        config.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String,
            PaymentProcessedEvent
            > paymentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String,
                PaymentProcessedEvent
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                paymentConsumerFactory()
        );

        return factory;
    }

    // =========================================================
    // SHIPMENT EVENT CONSUMER FACTORY
    // =========================================================

    @Bean
    public ConsumerFactory<String, ShipmentCreatedEvent>
    shipmentConsumerFactory() {

        JsonDeserializer<ShipmentCreatedEvent>
                deserializer =
                new JsonDeserializer<>(
                        ShipmentCreatedEvent.class
                );

        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        Map<String, Object> config =
                new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "order-shipment-group"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        config.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String,
            ShipmentCreatedEvent
            > shipmentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String,
                ShipmentCreatedEvent
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                shipmentConsumerFactory()
        );

        return factory;
    }


    @Bean
    public ConsumerFactory<String, InventoryRestoredEvent>
    inventoryRestoredConsumerFactory() {

        JsonDeserializer<InventoryRestoredEvent>
                deserializer =
                new JsonDeserializer<>(
                        InventoryRestoredEvent.class
                );

        deserializer.addTrustedPackages("*");

        deserializer.setUseTypeHeaders(false);

        Map<String, Object> config =
                new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "order-return-group"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        config.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String,
            InventoryRestoredEvent
            > inventoryRestoredKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String,
                InventoryRestoredEvent
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                inventoryRestoredConsumerFactory()
        );

        return factory;
    }
}