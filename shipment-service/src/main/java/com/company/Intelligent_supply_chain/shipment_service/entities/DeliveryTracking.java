package com.company.Intelligent_supply_chain.shipment_service.entities;


import com.company.Intelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_tracking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    private Long orderId;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime lastUpdated;
}
