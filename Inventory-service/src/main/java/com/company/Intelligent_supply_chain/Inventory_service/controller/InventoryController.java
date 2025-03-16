package com.company.Intelligent_supply_chain.Inventory_service.controller;

import com.company.Intelligent_supply_chain.Inventory_service.dtos.*;
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



    // reserve stock for the order
    @PostMapping("/reserve")
    public ResponseEntity<StockResponse> reserveStock(@RequestBody StockRequest request){
        boolean reserved = inventoryService.reserveStock(request.getSkuCode(), request.getQuantity());

        StockResponse response = StockResponse.builder()
                .skuCode(request.getSkuCode())
                .requestedQuantity(request.getQuantity())
                .quantity(inventoryService.getAvailableStock(request.getSkuCode())) // fetch updated stock
                .success(reserved)
                .message(reserved ? "Stock reserved successfully." : "Not enough stock available.")
                .build();
        return ResponseEntity.ok(response);
    }

    // Release reserved stock when an order is cancelled
    @PostMapping("/release")
    public ResponseEntity<StockResponse> releaseStock(@RequestBody StockRequest request){
        inventoryService.releaseStock(request.getSkuCode(), request.getQuantity());

        StockResponse response = StockResponse.builder()
                .skuCode(request.getSkuCode())
                .requestedQuantity(request.getQuantity())
                .quantity(inventoryService.getAvailableStock(request.getSkuCode()))
                .success(true)
                .message("Reserved stock released successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}