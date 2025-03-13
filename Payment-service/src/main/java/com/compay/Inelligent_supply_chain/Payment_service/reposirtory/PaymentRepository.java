package com.compay.Inelligent_supply_chain.Payment_service.reposirtory;

import com.compay.Inelligent_supply_chain.Payment_service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
