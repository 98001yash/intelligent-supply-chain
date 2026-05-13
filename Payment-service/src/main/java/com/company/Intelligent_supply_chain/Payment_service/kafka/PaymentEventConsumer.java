package com.company.Intelligent_supply_chain.Payment_service.kafka;

import com.company.Intelligent_supply_chain.Payment_service.entities.Payment;
import com.company.Intelligent_supply_chain.Payment_service.enums.PaymentStatus;
import com.company.Intelligent_supply_chain.Payment_service.reposirtory.PaymentRepository;

import com.company.intelligent_supply_chain.events.BaseEvent;
import com.company.intelligent_supply_chain.events.InventoryReservedEvent;
import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(
            topics = "inventory-reserved-topic",
            groupId = "payment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeInventoryReservedEvent(
            InventoryReservedEvent event
    ) {

        log.info(
                "Received InventoryReservedEvent for Order ID: {}",
                event.getOrderId()
        );

        // FORCE SUCCESS FOR TESTING
        boolean paymentSuccess = true;

        Payment payment =
                Payment.builder()
                        .orderId(event.getOrderId())
                        .amount(event.getTotalPrice())
                        .paymentMethod("CARD")
                        .status(
                                paymentSuccess
                                        ? PaymentStatus.SUCCESS
                                        : PaymentStatus.FAILED
                        )
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment saved in DB with status: {}",
                savedPayment.getStatus());

        PaymentProcessedEvent processedEvent =
                PaymentProcessedEvent.builder()
                        .eventId(BaseEvent.generateEventId())
                        .eventType("PAYMENT_PROCESSED")
                        .correlationId(event.getCorrelationId())
                        .timestamp(LocalDateTime.now())

                        .orderId(event.getOrderId())
                        .amount(event.getTotalPrice())
                        .paymentStatus(
                                savedPayment.getStatus().name()
                        )
                        .build();

        // DEBUG LOGS
        log.info("========== DEBUG PAYMENT STATUS ==========");
        log.info("paymentSuccess Boolean = {}", paymentSuccess);
        log.info("savedPayment.getStatus() = {}", savedPayment.getStatus());
        log.info("processedEvent.getPaymentStatus() = {}",
                processedEvent.getPaymentStatus());
        log.info("==========================================");

        paymentEventProducer
                .publishPaymentProcessedEvent(
                        processedEvent
                );

        log.info(
                "PaymentProcessedEvent published for Order ID: {}",
                event.getOrderId()
        );
    }
}