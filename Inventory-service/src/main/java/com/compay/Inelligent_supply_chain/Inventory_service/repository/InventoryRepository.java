package com.compay.Inelligent_supply_chain.Inventory_service.repository;

import com.compay.Inelligent_supply_chain.Inventory_service.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findBySkuCode(String skuCode);

    Optional<Inventory> findByProductId(Long productId);
}
