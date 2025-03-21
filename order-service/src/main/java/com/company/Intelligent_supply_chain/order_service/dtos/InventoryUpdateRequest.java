package com.company.Intelligent_supply_chain.order_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateRequest {
    private String skuCode;
    private Integer quantity;
}
