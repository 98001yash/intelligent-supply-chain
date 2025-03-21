package com.company.Intelligent_supply_chain.order_service.repository;

import com.company.Intelligent_supply_chain.order_service.entities.Order;
import com.company.Intelligent_supply_chain.order_service.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByCustomerId(Long customerId);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
