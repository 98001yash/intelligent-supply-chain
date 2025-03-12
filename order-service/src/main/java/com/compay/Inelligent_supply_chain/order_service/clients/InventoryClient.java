package com.compay.Inelligent_supply_chain.order_service.clients;


import com.compay.Inelligent_supply_chain.order_service.dtos.InventoryUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/check/{productId}")
    Boolean checkStock(@PathVariable Long productId);

    @PutMapping("/inventory/update")
    void updateStock(@RequestBody InventoryUpdateRequest request);
}
