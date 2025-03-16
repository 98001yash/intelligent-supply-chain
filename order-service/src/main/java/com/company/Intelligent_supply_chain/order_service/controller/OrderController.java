package com.company.Intelligent_supply_chain.order_service.controller;


import com.company.Intelligent_supply_chain.order_service.dtos.OrderDto;
import com.company.Intelligent_supply_chain.order_service.dtos.PaymentDto;
import com.company.Intelligent_supply_chain.order_service.enums.PaymentStatus;
import com.company.Intelligent_supply_chain.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.placeOrder(orderDto);
        // Publish the ORDER_PLACED event
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        try {
            log.info("📢 Received request to update order status. Order ID: {}, Status: {}", orderId, status);
            orderService.updateOrderStatus(orderId, status.toUpperCase());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("❌ Error updating order status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId){
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PostMapping("/payment/update")
    public ResponseEntity<String> handlePaymentUpdate(@RequestBody PaymentDto paymentDto) {
        orderService.handlePaymentUpdate(paymentDto);
        return ResponseEntity.ok("Payment update processed");
    }

}
