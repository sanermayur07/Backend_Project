package com.shoppingcart.service;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import com.shoppingcart.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // Save order item
    public OrderItem saveOrderItem(OrderItem orderItem) {

        return orderItemRepository.save(orderItem);
    }

    // Get order item by ID
    public OrderItem getOrderItemById(Long orderItemId) {

        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order item not found"));
    }

    // Get all items of an order
    public List<OrderItem> getItemsByOrder(Order order) {

        return orderItemRepository.findByOrder(order);
    }

    // Delete order item
    public void deleteOrderItem(Long orderItemId) {

        if (!orderItemRepository.existsById(orderItemId)) {
            throw new RuntimeException(
                    "Order item not found");
        }

        orderItemRepository.deleteById(orderItemId);
    }
}