package com.compay.Inelligent_supply_chain.order_service.service;


import com.compay.Inelligent_supply_chain.order_service.dtos.OrderDto;
import com.compay.Inelligent_supply_chain.order_service.entities.Order;
import com.compay.Inelligent_supply_chain.order_service.enums.OrderStatus;
import com.compay.Inelligent_supply_chain.order_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderDto createOrder(OrderDto orderDto){
        Order order  = modelMapper.map(orderDto, Order.class);
        order.setOrderStatus(OrderStatus.PENDING);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    public List<OrderDto> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto .class))
                .collect(Collectors.toList());
    }

    public OrderDto updateOrderStatus(Long orderId, OrderStatus status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with id: "+orderId));
        order.setOrderStatus(status);
        return modelMapper.map(orderRepository.save(order),OrderDto.class);
    }

    @Transactional
    public void deleteOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with Id: "+orderId));
        orderRepository.delete(order);
    }
}
