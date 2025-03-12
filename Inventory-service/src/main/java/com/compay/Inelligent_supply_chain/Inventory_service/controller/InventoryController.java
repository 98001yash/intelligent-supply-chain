package com.compay.Inelligent_supply_chain.Inventory_service.controller;

import com.compay.Inelligent_supply_chain.Inventory_service.dtos.InventoryRequest;
import com.compay.Inelligent_supply_chain.Inventory_service.dtos.InventoryResponse;
import com.compay.Inelligent_supply_chain.Inventory_service.dtos.InventoryUpdateRequest;
import com.compay.Inelligent_supply_chain.Inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<String> addInventory(@RequestBody InventoryRequest request){
        inventoryService.addInventory(request);
        return ResponseEntity.ok("inventory added successfully");
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory(){
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> getInventoryBySku(@PathVariable String skuCode){
        return ResponseEntity.ok(inventoryService.getInventoryBySkuCode(skuCode));
    }

    @PutMapping("/{skuCode}")
    public ResponseEntity<String> updateInventory(@PathVariable String skuCode, @RequestParam Integer quantity){
        inventoryService.updateInventory(skuCode, quantity);
        return ResponseEntity.ok("inventory updated successfully..!");
    }

    @GetMapping("/check/{productId}")
    public Boolean checkStock(@PathVariable Long productId) {
        return inventoryService.isProductAvailable(productId);
    }

    @PutMapping("/update")
    public void updateStock(@RequestBody InventoryUpdateRequest request) {
        inventoryService.updateStock(request.getProductId(), request.getQuantity());
    }
}
