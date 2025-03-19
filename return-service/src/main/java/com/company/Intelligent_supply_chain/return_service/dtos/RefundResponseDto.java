package com.company.Intelligent_supply_chain.return_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundResponseDto {

    private Long id;
    private Long returnRequest;
    private Long orderId;
    private Double amount;
    private String refundMethod;
    private LocalDateTime processedDate;
}
