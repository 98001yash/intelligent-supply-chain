package com.company.Intelligent_supply_chain.return_service.repository;

import com.company.Intelligent_supply_chain.return_service.dtos.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    Optional<ReturnRequest> findByOrderId(Long orderId);
}
