package com.compay.Inelligent_supply_chain.shipment_service.entities;


import com.compay.Inelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "courier_assignments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourierAssignment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courierId;
    private Long orderId;
    private String courierName;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
