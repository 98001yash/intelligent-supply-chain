package com.compay.Inelligent_supply_chain.shipment_service.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class EtaPredictionService {

    // predict ETA based on distance (simplified fpr you)

    public LocalDateTime predictEta(double distanceInKm) {
        log.info(" Predicting ETA for distance: {} km", distanceInKm);

        // Assuming average delivery speed = 40 km/h
        double estimatedHours = distanceInKm / 40.0;

        LocalDateTime estimatedArrivalTime = LocalDateTime.now().plus(Duration.ofMinutes((long) (estimatedHours * 60)));
        log.info(" Estimated Delivery Time: {}", estimatedArrivalTime);

        return estimatedArrivalTime;
    }
}
