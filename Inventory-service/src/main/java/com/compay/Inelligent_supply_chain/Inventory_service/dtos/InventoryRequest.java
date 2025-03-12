package com.compay.Inelligent_supply_chain.Inventory_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequest {

    private String skuCode;
    private String productName;
    private Integer quantity;
    private Double price;
}
