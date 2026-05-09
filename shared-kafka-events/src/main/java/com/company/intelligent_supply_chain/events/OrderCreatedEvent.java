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
public class OrderCreatedEvent extends BaseEvent{

    private Long orderId;
    private Long customerId;
    private Long skuCode;
    private Integer quantity;
    private Double totalPrice;
}
