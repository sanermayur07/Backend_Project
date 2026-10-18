package com.shoppingcart.repository;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    // Check whether payment already exists for an order
    boolean existsByOrder(Order order);

    // Find payment using order
    Optional<Payment> findByOrder(Order order);

    // Find payment using transaction ID
    Optional<Payment> findByTransactionId(String transactionId);
}