package com.compay.Inelligent_supply_chain.shipment_service.service;

import com.compay.Inelligent_supply_chain.shipment_service.dtos.DeliveryTrackingDto;
import com.compay.Inelligent_supply_chain.shipment_service.dtos.UpdateLiveTrackingRequest;
import com.compay.Inelligent_supply_chain.shipment_service.dtos.UpdateDeliveryStatusRequest;
import com.compay.Inelligent_supply_chain.shipment_service.entities.DeliveryTracking;
import com.compay.Inelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import com.compay.Inelligent_supply_chain.shipment_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.shipment_service.repositories.DeliveryTrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryTrackingService {

    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final ModelMapper modelMapper;

    public DeliveryTrackingDto updateLiveTracking(UpdateLiveTrackingRequest request) {
        log.info("Updating live tracking for Shipment ID: {} to [{}, {}]",
               request.getOrderId(), request.getShipmentId(), request.getLatitude(), request.getLongitude());

        DeliveryTracking tracking = deliveryTrackingRepository.findByShipmentId(request.getShipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found for Shipment ID: " + request.getShipmentId()));


        tracking.setOrderId(request.getOrderId());
        tracking.setLatitude(request.getLatitude());
        tracking.setLongitude(request.getLongitude());
        tracking.setLastUpdated(LocalDateTime.now());

        deliveryTrackingRepository.save(tracking);
        log.info("Live tracking updated: {}", tracking);

        return modelMapper.map(tracking, DeliveryTrackingDto.class);
    }

    public DeliveryTrackingDto updateDeliveryStatus(UpdateDeliveryStatusRequest request) {
        log.info("Updating delivery status for Shipment ID: {} to {}", request.getShipmentId(), request.getStatus());

        DeliveryTracking tracking = deliveryTrackingRepository.findByShipmentId(request.getShipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found for Shipment ID: " + request.getShipmentId()));

        tracking.setStatus(request.getStatus());
        tracking.setLastUpdated(LocalDateTime.now());

        deliveryTrackingRepository.save(tracking);
        log.info("Delivery status updated: {}", tracking);

        return modelMapper.map(tracking, DeliveryTrackingDto.class);
    }


}
