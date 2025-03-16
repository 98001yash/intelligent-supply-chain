package com.company.Intelligent_supply_chain.Payment_service.reposirtory;

import com.company.Intelligent_supply_chain.Payment_service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderId(Long orderId);
}
