package com.compay.Inelligent_supply_chain.Payment_service.clients;

import com.compay.Inelligent_supply_chain.Payment_service.enums.PaymentStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderClient {
    @PutMapping("/{orderId}/status")
    void updateOrderStatus(@PathVariable Long orderId, @RequestParam PaymentStatus status);
}