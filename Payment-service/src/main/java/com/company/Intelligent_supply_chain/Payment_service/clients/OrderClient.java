package com.company.Intelligent_supply_chain.Payment_service.clients;

import com.company.Intelligent_supply_chain.Payment_service.enums.PaymentStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "http://localhost:8080") // Change port if needed
public interface OrderClient {
    @PutMapping("/orders/{orderId}/status")
    void updateOrderStatus(@PathVariable("orderId") Long orderId,
                           @RequestParam("status") String status);
}
