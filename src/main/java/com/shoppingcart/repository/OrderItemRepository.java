package com.shoppingcart.repository;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Check whether a product is already used in an order
    boolean existsByProduct_ProductId(Long productId);

    // Get all items belonging to an order
    List<OrderItem> findByOrder(Order order);
}