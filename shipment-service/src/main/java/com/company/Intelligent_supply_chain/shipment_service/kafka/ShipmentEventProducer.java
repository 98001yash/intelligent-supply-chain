package com.company.Intelligent_supply_chain.shipment_service.kafka;


import com.company.intelligent_supply_chain.events.ShipmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventProducer {

    private final KafkaTemplate<String,
            ShipmentCreatedEvent> kafkaTemplate;

    private static final String TOPIC =
            "shipment-created-topic";

    public void publishShipmentCreatedEvent(
            ShipmentCreatedEvent event) {

        log.info("Publishing ShipmentCreatedEvent for Order ID: {}",
                event.getOrderId());

        kafkaTemplate.send(
                TOPIC,
                event.getOrderId().toString(),
                event
        );
        log.info("ShipmentCreatedEvent published successfully for Order ID: {}",
                event.getOrderId());
    }
}