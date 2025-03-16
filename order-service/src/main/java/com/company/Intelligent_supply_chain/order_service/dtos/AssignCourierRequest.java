package com.company.Intelligent_supply_chain.order_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCourierRequest {

    private Long orderId;
    private String courierName;
}
