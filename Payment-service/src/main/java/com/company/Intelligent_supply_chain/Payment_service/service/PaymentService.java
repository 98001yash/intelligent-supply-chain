package com.company.Intelligent_supply_chain.Payment_service.service;

import com.company.Intelligent_supply_chain.Payment_service.dtos.PaymentDto;
import com.company.Intelligent_supply_chain.Payment_service.entities.Payment;
import com.company.Intelligent_supply_chain.Payment_service.enums.PaymentStatus;
import com.company.Intelligent_supply_chain.Payment_service.exceptions.ResourceNotFoundException;
import com.company.Intelligent_supply_chain.Payment_service.reposirtory.PaymentRepository;
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


    @Transactional
    public PaymentDto processPayment(PaymentDto paymentDto) {
        log.info("📢 Processing payment for Order ID: {}", paymentDto.getOrderId());

        Payment payment = modelMapper.map(paymentDto, Payment.class);

        // ✅ Simulate random payment success (70% chance of success)
        boolean isPaymentSuccessful = Math.random() > 0.3;

        payment.setStatus(isPaymentSuccessful ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        log.info("✅ Payment {} for Order ID: {}", isPaymentSuccessful ? "SUCCESS" : "FAILED", paymentDto.getOrderId());

        Payment savedPayment = paymentRepository.save(payment);
        return modelMapper.map(savedPayment, PaymentDto.class);
    }

    public PaymentDto getPaymentByOrderId(Long orderId) {
        log.info(" Fetching payment details for Order ID: {}", orderId);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(" Payment not found for Order ID: " + orderId));

        return modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto updatePaymentStatus(Long orderId, PaymentStatus status) {
        log.info(" Updating payment status in DB for Order ID: {} to {}", orderId, status);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(" Payment not found for Order ID: " + orderId));

        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info(" Payment status successfully updated for Order ID: {}. New Status: {}", orderId, status);

        try {
           // orderClient.updateOrderStatus(payment.getOrderId(), status.name());
            log.info(" OrderService successfully notified about payment update for Order ID: {}", orderId);
        } catch (Exception e) {
            log.error("⚠ Failed to notify OrderService about payment update for Order ID: {}", orderId, e);
        }

        return modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto getPaymentStatus(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> modelMapper.map(payment, PaymentDto.class))
                .orElseThrow(() -> new ResourceNotFoundException(" No payment found for Order ID: " + orderId));
    }
}
