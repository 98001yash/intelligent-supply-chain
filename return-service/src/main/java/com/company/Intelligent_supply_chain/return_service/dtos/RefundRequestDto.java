package com.company.Intelligent_supply_chain.return_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequestDto {

    private Long returnRequestId;
    private Long orderId;
    private Double amount;
    private String refundMethod;
}
