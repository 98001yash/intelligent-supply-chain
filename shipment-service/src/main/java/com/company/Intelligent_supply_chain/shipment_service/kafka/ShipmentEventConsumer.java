package com.company.Intelligent_supply_chain.shipment_service.kafka;

import com.company.Intelligent_supply_chain.shipment_service.entities.Shipment;
import com.company.Intelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import com.company.Intelligent_supply_chain.shipment_service.repositories.ShipmentRepository;
import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;
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

    @KafkaListener(
            topics = "payment-processed-topic",
            groupId = "shipment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentProcessedEvent(
            PaymentProcessedEvent event) {

        log.info("Received PaymentProcessedEvent for Order ID: {}",
                event.getOrderId());

        // IMPORTANT
        // Only create shipment if payment successful

        if(!"SUCCESS".equalsIgnoreCase(
                event.getPaymentStatus())) {

            log.warn("Payment failed for Order ID: {}. Shipment will not be created.",
                    event.getOrderId());

            return;
        }

        // IDEMPOTENCY CHECK
        boolean shipmentExists =
                shipmentRepository
                        .findByOrderId(event.getOrderId())
                        .isPresent();

        if(shipmentExists) {

            log.warn("Shipment already exists for Order ID: {}",
                    event.getOrderId());

            return;
        }

        Shipment shipment = Shipment.builder()
                .orderId(event.getOrderId())
                .status(DeliveryStatus.CREATED)
                .estimatedDeliveryTime(
                        LocalDateTime.now().plusDays(5)
                )
                .build();

        Shipment savedShipment =
                shipmentRepository.save(shipment);

        log.info("Shipment created successfully for Order ID: {} with Shipment ID: {}",
                savedShipment.getOrderId(),
                savedShipment.getShipmentId());
    }
}