package com.company.Intelligent_supply_chain.shipment_service.repositories;

import com.company.Intelligent_supply_chain.shipment_service.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByOrderId(Long orderId);
}
