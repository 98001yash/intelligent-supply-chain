package com.compay.Inelligent_supply_chain.shipment_service.dtos;

import com.compay.Inelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import lombok.Data;

@Data
public class UpdateDeliveryStatusRequest {
    private Long shipmentId;
    private DeliveryStatus status;
}