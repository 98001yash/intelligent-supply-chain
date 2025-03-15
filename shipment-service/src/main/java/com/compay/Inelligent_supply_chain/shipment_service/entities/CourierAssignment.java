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
    private Long id;
    private Long orderId;

    private Long courierId;

    private LocalDateTime assignmentAt;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
