package com.company.intelligent_supply_chain.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RefundProcessedEvent extends BaseEvent {

    private Long returnRequestId;

    private Long orderId;
    private Double refundAmount;

    //  SUCCESS / FAILED
    private String refundStatus;
    private String refundMethod;
}
