package com.compay.Inelligent_supply_chain.shipment_service.controller;


import com.compay.Inelligent_supply_chain.shipment_service.dtos.CourierAssignmentDto;
import com.compay.Inelligent_supply_chain.shipment_service.dtos.DeliveryTrackingDto;
import com.compay.Inelligent_supply_chain.shipment_service.entities.CourierAssignment;
import com.compay.Inelligent_supply_chain.shipment_service.entities.DeliveryTracking;
import com.compay.Inelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import com.compay.Inelligent_supply_chain.shipment_service.service.CourierAssignmentService;
import com.compay.Inelligent_supply_chain.shipment_service.service.DeliveryTrackingService;
import com.compay.Inelligent_supply_chain.shipment_service.service.EtaPredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
@Slf4j
public class ShipmentController {

    private final CourierAssignmentService courierAssignmentService;
    private final DeliveryTrackingService deliveryTrackingService;
    private final EtaPredictionService etaPredictionService;


    @PostMapping("/assign-courier")
    public ResponseEntity<CourierAssignmentDto> assignCourier(
            @RequestParam Long orderId,
            @RequestParam String courierName
    ){
        log.info("Assigning courier '{}' to order ID: {}", courierName, orderId);
        CourierAssignmentDto assignedCourier = courierAssignmentService.assignCourier(orderId, courierName);
        return ResponseEntity.ok(assignedCourier);
    }


    @PutMapping("/courier-status")
    public ResponseEntity<CourierAssignmentDto> updateCourierStatus(
            @RequestParam Long orderId,
            @RequestParam DeliveryStatus status
            ){
        log.info("Updating courier status for order ID: {} to {}",orderId, status);
        CourierAssignmentDto updatedCourier = courierAssignmentService.updateCourierStatus(orderId, status);
        return ResponseEntity.ok(updatedCourier);
    }


    @PutMapping("/update-tracking")
    public ResponseEntity<DeliveryTrackingDto> updateLiveTracking(
            @RequestParam Long shipmentId,
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ){
        log.info("Updating live tracking for shipment ID: {}",shipmentId);
        DeliveryTrackingDto updatedTracking = deliveryTrackingService.updateLiveTracking(shipmentId, latitude, longitude);
        return ResponseEntity.ok(updatedTracking);
    }

    @PutMapping("/update-delivery-status")
    public ResponseEntity<DeliveryTrackingDto> updateDeliveryStatus(
            @RequestParam Long shipmentId,
            @RequestParam DeliveryStatus status) {

        log.info(" Updating delivery status for Shipment ID: {} to {}", shipmentId, status);
        DeliveryTrackingDto updatedStatus = deliveryTrackingService.updateDeliveryStatus(shipmentId, status);
        return ResponseEntity.ok(updatedStatus);
    }

    @GetMapping("/predict-eta")
    public ResponseEntity<LocalDateTime> predictEta(@RequestParam double distanceInKm) {
        log.info(" Predicting ETA for distance: {} km", distanceInKm);
        LocalDateTime estimatedArrival = etaPredictionService.predictEta(distanceInKm);
        return ResponseEntity.ok(estimatedArrival);
    }
}
