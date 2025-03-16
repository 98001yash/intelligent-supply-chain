package com.company.Intelligent_supply_chain.shipment_service.dtos;


import com.company.Intelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierAssignmentDto {
    private Long orderId;
    private Long courierId;
    private String courierName;
    private LocalDateTime assignedAt;
    private DeliveryStatus status;

}
