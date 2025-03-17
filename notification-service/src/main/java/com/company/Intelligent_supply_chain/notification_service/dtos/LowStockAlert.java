package com.company.Intelligent_supply_chain.notification_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowStockAlert {
    private String skuCode;
    private int quantity;
}