package com.shoppingcart.controller;

import com.shoppingcart.dto.OrderItemResponse;

import com.shoppingcart.dto.OrderResponse;
import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import com.shoppingcart.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    // ==========================================
    // CHECKOUT
    // ==========================================

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(
            @RequestParam Long userId) {

        Order order =
                orderService.checkout(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }


    // ==========================================
    // GET ORDER BY ID
    // ==========================================

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId) {

        Order order =
                orderService.getOrderById(orderId);

        return ResponseEntity.ok(
                convertToResponse(order)
        );
    }


    // ==========================================
    // GET USER ORDERS
    // ==========================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @PathVariable Long userId) {

        List<Order> orders =
                orderService.getUserOrders(userId);

        List<OrderResponse> response =
                orders.stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET ALL ORDERS
    // ADMIN
    // ==========================================

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<Order> orders =
                orderService.getAllOrders();

        List<OrderResponse> response =
                orders.stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // UPDATE ORDER STATUS
    // ADMIN
    // ==========================================

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        Order order = orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(convertToResponse(order));
    }

    // ==========================================
    // ENTITY → DTO
    // ==========================================

    private OrderResponse convertToResponse(Order order) {

        List<OrderItemResponse> items =
                order.getOrderItems()
                        .stream()
                        .map(this::convertItemToResponse)
                        .collect(Collectors.toList());

        return new OrderResponse(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                items
        );
    }

    // ==========================================
    // ORDER ITEM → DTO
    // ==========================================

    private OrderItemResponse convertItemToResponse(
            OrderItem item) {

        return new OrderItemResponse(
                item.getOrderItemId(),
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getPrice()
                        .multiply(
                                java.math.BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        )
        );
    }
}