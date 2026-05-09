package com.company.intelligent_supply_chain.events;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaymentProcessedEvent {

    private Long orderId;
    private Double amount;

    private String paymentStatus;
}
