package com.compay.Inelligent_supply_chain.Inventory_service.service;

import com.compay.Inelligent_supply_chain.Inventory_service.dtos.InventoryRequest;
import com.compay.Inelligent_supply_chain.Inventory_service.dtos.InventoryResponse;
import com.compay.Inelligent_supply_chain.Inventory_service.entities.Inventory;
import com.compay.Inelligent_supply_chain.Inventory_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.Inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    public InventoryResponse addInventory(InventoryRequest request) {
        Inventory inventory = modelMapper.map(request, Inventory.class);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return modelMapper.map(savedInventory, InventoryResponse.class);
    }

    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(inventory -> modelMapper.map(inventory, InventoryResponse.class))
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryBySkuCode(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for SKU: " + skuCode));
        return modelMapper.map(inventory, InventoryResponse.class);
    }

    @Transactional
    public InventoryResponse updateInventory(String skuCode, Integer newQuantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for SKU: " + skuCode));

        if (newQuantity <= 0) {
            throw new RuntimeException("Invalid quantity update");
        }

        inventory.setQuantity(inventory.getQuantity() + newQuantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        log.info("Inventory updated for SKU: {} with new quantity: {}", skuCode, updatedInventory.getQuantity());
        return modelMapper.map(updatedInventory, InventoryResponse.class);
    }

    public boolean isProductAvailable(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventory -> inventory.getQuantity() > 0)
                .orElse(false);
    }

    @Transactional
    public void updateStock(String skuCode, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
        log.info("Stock updated for SKU: {}. New quantity: {}", skuCode, inventory.getQuantity());
    }
}
