package com.shoppingcart.repository;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders of a particular user
    List<Order> findByUser(User user);

    // Find orders using user ID
    List<Order> findByUserUserId(Long userId);
}