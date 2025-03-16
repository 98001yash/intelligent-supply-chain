package com.company.Intelligent_supply_chain.order_service.dtos;


import com.company.Intelligent_supply_chain.order_service.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private Long orderId;
    private Double amount;
    private PaymentStatus status;
}
