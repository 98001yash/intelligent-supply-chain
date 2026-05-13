package com.company.Intelligent_supply_chain.shipment_service.kafka;

import com.company.Intelligent_supply_chain.shipment_service.entities.Shipment;
import com.company.Intelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import com.company.Intelligent_supply_chain.shipment_service.repositories.ShipmentRepository;
import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;
import com.company.intelligent_supply_chain.events.ShipmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventConsumer {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentEventProducer shipmentEventProducer;

    @KafkaListener(
            topics = "payment-processed-topic",
            groupId = "shipment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentProcessedEvent(
            PaymentProcessedEvent event) {

        log.info("Received PaymentProcessedEvent for Order ID: {}",
                event.getOrderId());

        // ONLY CREATE SHIPMENT IF PAYMENT SUCCESSFUL
        if (!"SUCCESS".equalsIgnoreCase(
                event.getPaymentStatus())) {

            log.warn(
                    "Payment failed for Order ID: {}. Shipment will not be created.",
                    event.getOrderId()
            );

            return;
        }

        // IDEMPOTENCY CHECK

        boolean shipmentExists =
                shipmentRepository
                        .findByOrderId(event.getOrderId())
                        .isPresent();

        if (shipmentExists) {

            log.warn(
                    "Shipment already exists for Order ID: {}",
                    event.getOrderId()
            );

            return;
        }

        // CREATE SHIPMENT
        Shipment shipment = Shipment.builder()
                .orderId(event.getOrderId())
                .status(DeliveryStatus.CREATED)
                .estimatedDeliveryTime(
                        LocalDateTime.now().plusDays(5)
                )
                .build();

        Shipment savedShipment =
                shipmentRepository.save(shipment);

        log.info(
                "Shipment created successfully for Order ID: {} with Shipment ID: {}",
                savedShipment.getOrderId(),
                savedShipment.getShipmentId()
        );

        // CREATE EVENT
        ShipmentCreatedEvent shipmentCreatedEvent =
                ShipmentCreatedEvent.builder()
                        .shipmentId(savedShipment.getShipmentId())
                        .orderId(savedShipment.getOrderId())
                        .trackingNumber(savedShipment.getTrackingNumber())
                        .shipmentStatus(savedShipment.getStatus().name())
                        .courierName("NOT_ASSIGNED")
                        .build();

        // PUBLISH EVENT
        shipmentEventProducer
                .publishShipmentCreatedEvent(
                        shipmentCreatedEvent
                );

        log.info(
                "ShipmentCreatedEvent published for Order ID: {}",
                savedShipment.getOrderId()
        );
    }
}