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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    // add new inventory
    public void addInventory(InventoryRequest request){
        Inventory inventory = modelMapper.map(request, Inventory.class);
        inventoryRepository.save(inventory);
        log.info("Inventory saved: {}",inventory);
    }

    // get all inventory items
    public List<InventoryResponse> getAllInventory(){
        List<Inventory> inventoryList = inventoryRepository.findAll();
        return inventoryList.stream()
                .map(inventory->modelMapper.map(inventory, InventoryResponse.class))
                .collect(Collectors.toList());
    }

    // get inventory by SKU code
    public InventoryResponse getInventoryBySkuCode(String skuCode){
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(()->new ResourceNotFoundException("Inventory not found for SKU: "+skuCode));
        return modelMapper.map(inventory, InventoryResponse.class);
    }

    // update inventory quantity
    public void updateInventory(String skuCode, Integer newQuantity){
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(()->new ResourceNotFoundException("Inventory not found for SKU: "+skuCode));
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        log.info("Inventory updated for SKU: {}", skuCode);
    }
}
