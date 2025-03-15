package com.compay.Inelligent_supply_chain.Payment_service.controller;

import com.compay.Inelligent_supply_chain.Payment_service.dtos.PaymentDto;
import com.compay.Inelligent_supply_chain.Payment_service.enums.PaymentStatus;
import com.compay.Inelligent_supply_chain.Payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> processPayment(@RequestBody PaymentDto paymentDto) {
        log.info("Received payment request for order ID: {}", paymentDto.getOrderId());
        PaymentDto processedPayment = paymentService.processPayment(paymentDto);
        return ResponseEntity.ok(processedPayment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentByOrderId(@PathVariable Long orderId) {
        log.info("Fetching payment details for order ID: {}", orderId);
        PaymentDto paymentDto = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(paymentDto);
    }

    @PatchMapping("/order/{orderId}/status")
    public ResponseEntity<PaymentDto> updatePaymentStatusByOrderId(
            @PathVariable Long orderId,
            @RequestParam String status) {

        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase()); // Convert String to Enum
            PaymentDto updatedPayment = paymentService.updatePaymentStatus(orderId, paymentStatus);
            return ResponseEntity.ok(updatedPayment);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment status received: {}", status);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentStatus(@PathVariable Long orderId) {
        PaymentDto paymentStatus = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(paymentStatus);
    }
}
