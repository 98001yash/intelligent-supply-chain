package com.compay.Inelligent_supply_chain.Inventory_service.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String skuCode;
    private String productName;
    private Long productId;
    private Integer quantity;
    private Double price;
}
