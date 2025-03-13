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
    public ResponseEntity<PaymentDto> processPayment(@RequestBody PaymentDto paymentDto){
        log.info("Received payment request for order ID: {}",paymentDto.getOrderId());
        PaymentDto processedPayment= paymentService.processPayment(paymentDto);
        return ResponseEntity.ok(processedPayment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDto>getPaymentByOrderId(@PathVariable Long orderId){
        log.info("Fetching payment details for order ID: {}",orderId);
        PaymentDto paymentDto = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(paymentDto);
    }

    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDto> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {
        log.info("Updating payment status for Payment ID: {} to {}", paymentId, status);
        PaymentDto updatedPayment = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(updatedPayment);
    }
}
