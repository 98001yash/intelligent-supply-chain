package com.company.Intelligent_supply_chain.shipment_service.dtos;

import lombok.Data;

@Data
public class UpdateLiveTrackingRequest {

    private Long orderId;
    private Long shipmentId;
    private Double latitude;
    private Double longitude;
}