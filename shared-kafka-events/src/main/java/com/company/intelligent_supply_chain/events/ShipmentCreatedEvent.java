package com.company.intelligent_supply_chain.events;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ShipmentCreatedEvent extends BaseEvent {

    private Long shipmentId;
    private Long orderId;
    private String trackingNumber;
    private String shipmentStatus;
    private String courierName;
}