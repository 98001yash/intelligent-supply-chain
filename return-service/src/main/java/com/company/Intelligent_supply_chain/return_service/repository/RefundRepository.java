package com.company.Intelligent_supply_chain.return_service.repository;

import com.company.Intelligent_supply_chain.return_service.entities.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund,Long> {
    Optional<Refund> findByReturnRequestId(Long returnRequestId);
}
