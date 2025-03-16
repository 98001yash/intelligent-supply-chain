package com.compay.Inelligent_supply_chain.shipment_service.dtos;

import com.compay.Inelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import lombok.Data;

@Data
public class UpdateCourierStatusRequest {
    private Long orderId;
    private DeliveryStatus status;
}