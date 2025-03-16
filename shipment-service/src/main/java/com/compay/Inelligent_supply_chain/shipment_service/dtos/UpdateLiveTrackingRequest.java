package com.compay.Inelligent_supply_chain.shipment_service.dtos;

import lombok.Data;

@Data
public class UpdateLiveTrackingRequest {
    private Long shipmentId;
    private Double latitude;
    private Double longitude;
}