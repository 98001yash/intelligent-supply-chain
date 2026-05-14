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
public class InventoryRestoredEvent extends BaseEvent{

    private Long orderId;
    private String skuCode;
    private Integer restoredQuantity;

    // SUCCESS/ FAILED
    private String restoredStatus;
}
