package com.company.Intelligent_supply_chain.shipment_service.controller;

import com.company.Intelligent_supply_chain.shipment_service.dtos.*;
import com.company.Intelligent_supply_chain.shipment_service.service.CourierAssignmentService;
import com.company.Intelligent_supply_chain.shipment_service.service.DeliveryTrackingService;
import com.company.Intelligent_supply_chain.shipment_service.service.EtaPredictionService;
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
            @RequestBody AssignCourierRequest request) {
        log.info("Assigning courier '{}' to order ID: {}", request.getCourierName(), request.getOrderId());
        CourierAssignmentDto assignedCourier = courierAssignmentService.assignCourier(request.getOrderId(), request.getCourierName());
        return ResponseEntity.ok(assignedCourier);
    }

    @PutMapping("/courier-status")
    public ResponseEntity<CourierAssignmentDto> updateCourierStatus(
            @RequestBody UpdateCourierStatusRequest request) {
        log.info("Updating courier status for order ID: {} to {}", request.getOrderId(), request.getStatus());
        CourierAssignmentDto updatedCourier = courierAssignmentService.updateCourierStatus(request.getOrderId(), request.getStatus());
        return ResponseEntity.ok(updatedCourier);
    }

    @PutMapping("/update-tracking")
    public ResponseEntity<DeliveryTrackingDto> updateLiveTracking(
            @RequestBody UpdateLiveTrackingRequest request) {
        log.info("Updating live tracking for shipment ID: {}", request.getShipmentId());
        DeliveryTrackingDto updatedTracking = deliveryTrackingService.updateLiveTracking(request);
        return ResponseEntity.ok(updatedTracking);
    }



    @PutMapping("/update-delivery-status")
    public ResponseEntity<DeliveryTrackingDto> updateDeliveryStatus(
            @RequestBody UpdateDeliveryStatusRequest request) {
        log.info("Updating delivery status for Shipment ID: {} to {}", request.getShipmentId(), request.getStatus());
        DeliveryTrackingDto updatedStatus = deliveryTrackingService.updateDeliveryStatus(request);
        return ResponseEntity.ok(updatedStatus);
    }


    @PostMapping("/predict-eta")
    public ResponseEntity<LocalDateTime> predictEta(
            @RequestBody PredictEtaRequest request) {
        log.info("Predicting ETA for distance: {} km", request.getDistanceInKm());
        LocalDateTime estimatedArrival = etaPredictionService.predictEta(request.getDistanceInKm());
        return ResponseEntity.ok(estimatedArrival);
    }
}
