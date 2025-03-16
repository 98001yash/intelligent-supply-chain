package com.company.Intelligent_supply_chain.order_service.clients;


import com.company.Intelligent_supply_chain.order_service.dtos.InventoryUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/check/{skuCode}")
    Boolean checkStock(@PathVariable("skuCode") String skuCode);


    @PutMapping("/inventory/update")
    void updateStock(@RequestBody InventoryUpdateRequest request);

}
