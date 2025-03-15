package com.compay.Inelligent_supply_chain.shipment_service.repositories;

import com.compay.Inelligent_supply_chain.shipment_service.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryTrackingRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderId(Long orderId);
}
