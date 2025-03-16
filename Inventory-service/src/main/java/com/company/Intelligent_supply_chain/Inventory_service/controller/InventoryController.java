package com.company.Intelligent_supply_chain.Inventory_service.controller;

import com.company.Intelligent_supply_chain.Inventory_service.dtos.InventoryRequest;
import com.company.Intelligent_supply_chain.Inventory_service.dtos.InventoryResponse;
import com.company.Intelligent_supply_chain.Inventory_service.dtos.InventoryUpdateRequest;
import com.company.Intelligent_supply_chain.Inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> addInventory(@RequestBody InventoryRequest request) {
        log.info("Adding inventory: {}", request);
        InventoryResponse savedInventory = inventoryService.addInventory(request);
        return ResponseEntity.ok(savedInventory);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> getInventoryBySku(@PathVariable String skuCode) {
        return ResponseEntity.ok(inventoryService.getInventoryBySkuCode(skuCode));
    }

    @PutMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable String skuCode, @RequestParam Integer quantity) {
        InventoryResponse updatedInventory = inventoryService.updateInventory(skuCode, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @GetMapping("/check/{skuCode}")
    public ResponseEntity<Boolean> checkStock(@PathVariable("skuCode") String skuCode) {
        return ResponseEntity.ok(inventoryService.isProductAvailable(skuCode));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody InventoryUpdateRequest request) {
        log.info("Updating stock for SKU: {}", request.getSkuCode());
        inventoryService.updateStock(request.getSkuCode(), request.getQuantity());
        return ResponseEntity.ok("Inventory updated successfully!");
    }
}