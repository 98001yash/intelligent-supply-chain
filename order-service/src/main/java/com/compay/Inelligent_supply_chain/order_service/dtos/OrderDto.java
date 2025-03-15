package com.compay.Inelligent_supply_chain.order_service.dtos;



import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Long customerId;
    private Long supplierId;
    private String skuCode;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
