package com.company.Intelligent_supply_chain.Payment_service.config;



import com.company.intelligent_supply_chain.events.InventoryReservedEvent;
import com.company.intelligent_supply_chain.events.ReturnRequestedEvent;
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
    public ConsumerFactory<String, InventoryReservedEvent>
    consumerFactory() {

        JsonDeserializer<InventoryReservedEvent>
                deserializer =
                new JsonDeserializer<>(
                        InventoryReservedEvent.class
                );

        deserializer.addTrustedPackages("*");

        // IMPORTANT
        deserializer.setUseTypeHeaders(false);

        Map<String, Object> config =
                new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "payment-group"
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
    public ConsumerFactory<String, ReturnRequestedEvent>
    returnConsumerFactory() {

        JsonDeserializer<ReturnRequestedEvent>
                deserializer =
                new JsonDeserializer<>(
                        ReturnRequestedEvent.class
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
                "payment-return-group"
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
            InventoryReservedEvent
            > kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String,
                InventoryReservedEvent
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                consumerFactory()
        );
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String,
            ReturnRequestedEvent
            > returnKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String,
                ReturnRequestedEvent
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                returnConsumerFactory()
        );
        return factory;
    }

}