package com.company.Intelligent_supply_chain.shipment_service.repositories;

import com.company.Intelligent_supply_chain.shipment_service.entities.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {


    Optional<DeliveryTracking> findByShipmentId(Long shipmentId);
}

