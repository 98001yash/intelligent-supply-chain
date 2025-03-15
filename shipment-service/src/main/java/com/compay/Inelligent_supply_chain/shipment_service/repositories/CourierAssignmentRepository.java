package com.compay.Inelligent_supply_chain.shipment_service.repositories;

import com.compay.Inelligent_supply_chain.shipment_service.entities.CourierAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierAssignmentRepository extends JpaRepository<CourierAssignment,Long> {

    Optional<CourierAssignment> findByOrderId(Long orderId);
}
