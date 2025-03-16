package com.compay.Inelligent_supply_chain.shipment_service.dtos;

import lombok.Data;

@Data
public class AssignCourierRequest {
    private Long orderId;
    private String courierName;
}