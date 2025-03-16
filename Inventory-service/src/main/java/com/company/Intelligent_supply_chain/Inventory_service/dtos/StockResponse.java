package com.company.Intelligent_supply_chain.Inventory_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {

    private String skuCode;
    private Integer requestedQuantity;
    private Integer quantity;
    private boolean success;
    private String message;
}
