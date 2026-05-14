package com.company.Intelligent_supply_chain.Payment_service.kafka;



import com.company.Intelligent_supply_chain.Payment_service.entities.Payment;
import com.company.Intelligent_supply_chain.Payment_service.enums.PaymentStatus;
import com.company.Intelligent_supply_chain.Payment_service.reposirtory.PaymentRepository;

import com.company.intelligent_supply_chain.events.BaseEvent;
import com.company.intelligent_supply_chain.events.RefundProcessedEvent;
import com.company.intelligent_supply_chain.events.ReturnRequestedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnEventConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(
            topics = "return-requested-topic",
            groupId = "payment-return-group",
            containerFactory =
                    "returnKafkaListenerContainerFactory"
    )
    public void consumeReturnRequestedEvent(
            ReturnRequestedEvent event
    ) {
        log.info(
                "Received ReturnRequestedEvent for Order ID: {}",
                event.getOrderId()
        );

        // REFUND SUCCESS SIMULATION
        boolean refundSuccess = true;
        Payment refundPayment =
                Payment.builder()

                        .orderId(
                                event.getOrderId()
                        )
                        .amount(0.0)
                        .paymentMethod("REFUND")
                        .status(
                                refundSuccess
                                        ? PaymentStatus.SUCCESS
                                        : PaymentStatus.FAILED
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .updatedAt(
                                LocalDateTime.now()
                        )
                        .build();

        paymentRepository.save(refundPayment);
        log.info(
                "Refund processed for Order ID: {}",
                event.getOrderId()
        );
        RefundProcessedEvent refundEvent =
                RefundProcessedEvent.builder()

                        .eventId(
                                BaseEvent.generateEventId()
                        )
                        .eventType(
                                "REFUND_PROCESSED"
                        )
                        .correlationId(
                                event.getCorrelationId()
                        )
                        .timestamp(
                                LocalDateTime.now()
                        )
                        .returnRequestId(
                                event.getReturnRequestId()
                        )
                        .orderId(
                                event.getOrderId()
                        )
                        .refundAmount(
                                refundPayment.getAmount()
                        )
                        .refundStatus(
                                refundPayment
                                        .getStatus()
                                        .name()
                        )
                        .refundMethod(
                                refundPayment
                                        .getPaymentMethod()
                        )
                        .build();
        paymentEventProducer
                .publishRefundProcessedEvent(
                        refundEvent
                );

        log.info(
                "RefundProcessedEvent published for Order ID: {}",
                event.getOrderId()
        );
    }
}