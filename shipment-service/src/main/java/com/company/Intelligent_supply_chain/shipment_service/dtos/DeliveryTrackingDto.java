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
public class DeliveryTrackingDto {
    private Long shipmentId;
    private Long orderId;
    private Double latitude;
    private Double longitude;
    private DeliveryStatus status;
    private LocalDateTime lastUpdated;
}
