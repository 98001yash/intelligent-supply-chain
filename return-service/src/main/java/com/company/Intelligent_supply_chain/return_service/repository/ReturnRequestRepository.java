package com.company.Intelligent_supply_chain.return_service.repository;

import com.company.Intelligent_supply_chain.return_service.dtos.ReturnRequestDto;
import com.company.Intelligent_supply_chain.return_service.entities.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    Optional<ReturnRequestDto> findByOrderId(Long orderId);
}
