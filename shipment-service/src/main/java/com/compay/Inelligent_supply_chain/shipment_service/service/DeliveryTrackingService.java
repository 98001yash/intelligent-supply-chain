package com.compay.Inelligent_supply_chain.shipment_service.service;

import com.compay.Inelligent_supply_chain.shipment_service.dtos.DeliveryTrackingDto;
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


    public DeliveryTrackingDto updateLiveTracking(Long shipmentId, Double latitude, Double longitude) {
        log.info(" Updating live tracking for Shipment ID: {} to [{}, {}]", shipmentId, latitude, longitude);

        DeliveryTracking tracking = deliveryTrackingRepository.findByShipmentId(shipmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Tracking not found for Shipment ID: " + shipmentId));

        tracking.setLatitude(latitude);
        tracking.setLongitude(longitude);
        tracking.setLastUpdated(LocalDateTime.now());

        deliveryTrackingRepository.save(tracking);
        log.info(" Live tracking updated: {}", tracking);

        return modelMapper.map(tracking, DeliveryTrackingDto.class);
    }

    public DeliveryTrackingDto updateDeliveryStatus(Long shipmentId, DeliveryStatus status) {
        log.info("🚛 Updating delivery status for Shipment ID: {} to {}", shipmentId, status);

        DeliveryTracking tracking = deliveryTrackingRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found for Shipment ID: " + shipmentId));

        tracking.setStatus(status);
        tracking.setLastUpdated(LocalDateTime.now());

        deliveryTrackingRepository.save(tracking);
        log.info(" Delivery status updated: {}", tracking);

        return modelMapper.map(tracking, DeliveryTrackingDto.class);
    }
}