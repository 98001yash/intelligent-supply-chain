package com.company.Intelligent_supply_chain.return_service.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long returnRequestId;
    private Long orderId;
    private Double amount;

    private String status; // INITIATED, COMPLETED, FAILED
    private String refundMethod; //BANK_TRANSFER, WALLET, ORIGINAL_PAYMENT
    private LocalDateTime processedDate;
}
