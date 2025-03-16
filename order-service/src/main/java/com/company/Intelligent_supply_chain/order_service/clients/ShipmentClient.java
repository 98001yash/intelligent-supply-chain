package com.company.Intelligent_supply_chain.order_service.clients;

import com.company.Intelligent_supply_chain.order_service.dtos.AssignCourierRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shipment-service")
public interface ShipmentClient {

    @PostMapping("/shipments/assign-courier")
    void assignCourier(@RequestBody AssignCourierRequest request);
}
