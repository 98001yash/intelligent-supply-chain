package com.company.intelligent_supply_chain.events;



import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RefundProcessedEvent extends BaseEvent {

    private Long returnRequestId;

    private Long orderId;

    private Double refundAmount;

    // SUCCESS / FAILED
    private String refundStatus;

    private String refundMethod;

    // NEW
    private String skuCode;

    private Integer quantity;
}