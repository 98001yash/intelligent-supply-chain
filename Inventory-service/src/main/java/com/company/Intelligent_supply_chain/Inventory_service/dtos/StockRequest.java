package com.company.Intelligent_supply_chain.Inventory_service.dtos;


import lombok.Data;

@Data
public class StockRequest {

    private String skuCode;
    private Integer quantity;
}
