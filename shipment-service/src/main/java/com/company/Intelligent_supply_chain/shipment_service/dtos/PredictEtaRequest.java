package com.company.Intelligent_supply_chain.shipment_service.dtos;

import lombok.Data;

@Data
public class PredictEtaRequest {
    private double distanceInKm;
}