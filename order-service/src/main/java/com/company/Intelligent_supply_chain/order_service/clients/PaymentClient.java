package com.company.Intelligent_supply_chain.order_service.clients;



import com.company.Intelligent_supply_chain.order_service.dtos.PaymentDto;
import com.company.Intelligent_supply_chain.order_service.enums.PaymentStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "payment-service", path="/payments")
public interface  PaymentClient {


    @PostMapping
    PaymentDto processPayment(@RequestBody PaymentDto paymentDto);

    @GetMapping("/order/{orderId}")
    PaymentDto getPaymentByOrderId(@PathVariable Long orderId);

    @PatchMapping("/{paymentId}/status")
    PaymentDto updatePaymentStatus(@PathVariable Long paymentId, @RequestParam PaymentStatus status);

    @GetMapping("/payments/status/{orderId}")
    PaymentDto getPaymentStatus(@PathVariable("orderId") Long orderId);

}
