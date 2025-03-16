package com.compay.Inelligent_supply_chain.shipment_service.repositories;

import com.compay.Inelligent_supply_chain.shipment_service.entities.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {

    Optional<DeliveryTracking> findByOrderId(Long orderId);

    Optional<DeliveryTracking> findByShipmentId(Long shipmentId);
}

