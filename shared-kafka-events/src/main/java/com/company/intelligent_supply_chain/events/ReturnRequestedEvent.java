package com.company.intelligent_supply_chain.events;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReturnRequestedEvent extends BaseEvent {

    private Long returnRequestId;

    private Long orderId;

    private String reason;

    private String returnStatus;

    private String skuCode;
    private Integer quantity;
}