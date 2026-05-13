package com.company.Intelligent_supply_chain.Payment_service.service;

import com.company.Intelligent_supply_chain.Payment_service.dtos.PaymentDto;
import com.company.Intelligent_supply_chain.Payment_service.entities.Payment;
import com.company.Intelligent_supply_chain.Payment_service.enums.PaymentStatus;
import com.company.Intelligent_supply_chain.Payment_service.exceptions.ResourceNotFoundException;
import com.company.Intelligent_supply_chain.Payment_service.kafka.PaymentEventProducer;
import com.company.Intelligent_supply_chain.Payment_service.reposirtory.PaymentRepository;

import com.company.intelligent_supply_chain.events.BaseEvent;
import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final PaymentEventProducer paymentEventProducer;


    @Transactional
    public PaymentDto processPayment(PaymentDto paymentDto) {

        log.info(
                "Processing payment for Order ID: {}",
                paymentDto.getOrderId()
        );

        Payment payment =
                modelMapper.map(paymentDto, Payment.class);

        // Simulate payment processing
        boolean isPaymentSuccessful = true;

        payment.setStatus(
                isPaymentSuccessful
                        ? PaymentStatus.SUCCESS
                        : PaymentStatus.FAILED
        );

        log.info(
                "Payment {} for Order ID: {}",
                isPaymentSuccessful
                        ? "SUCCESS"
                        : "FAILED",
                paymentDto.getOrderId()
        );
        Payment savedPayment =
                paymentRepository.save(payment);

        PaymentProcessedEvent event =
                PaymentProcessedEvent.builder()
                        .eventId(
                                BaseEvent.generateEventId()
                        )
                        .eventType(
                                "PAYMENT_PROCESSED"
                        )
                        .correlationId(
                                savedPayment.getOrderId().toString()
                        )
                        .timestamp(
                                LocalDateTime.now()
                        )
                        .orderId(
                                savedPayment.getOrderId()
                        )
                        .amount(
                                savedPayment.getAmount()
                        )
                        .paymentStatus(
                                savedPayment.getStatus().name()
                        )
                        .build();

        paymentEventProducer
                .publishPaymentProcessedEvent(event);
        log.info(
                "PaymentProcessedEvent published for Order ID: {}",
                savedPayment.getOrderId()
        );
        return modelMapper.map(savedPayment, PaymentDto.class);
    }


    public PaymentDto getPaymentByOrderId(Long orderId) {
        log.info(
                "Fetching payment details for Order ID: {}",
                orderId
        );

        Payment payment =
                paymentRepository.findByOrderId(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for Order ID: "
                                                + orderId
                                )
                        );

        return modelMapper.map(payment, PaymentDto.class);
    }


    public PaymentDto updatePaymentStatus(
            Long orderId,
            PaymentStatus status
    ) {
        log.info(
                "Updating payment status in DB for Order ID: {} to {}",
                orderId,
                status
        );
        Payment payment =
                paymentRepository.findByOrderId(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for Order ID: "
                                                + orderId
                                )
                        );
        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info(
                "Payment status successfully updated for Order ID: {}",
                orderId
        );
        return modelMapper.map(payment, PaymentDto.class);
    }


    public PaymentDto getPaymentStatus(Long orderId) {

        return paymentRepository.findByOrderId(orderId)
                .map(payment ->
                        modelMapper.map(payment, PaymentDto.class)
                )

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No payment found for Order ID: "
                                        + orderId
                        )
                );
    }
}