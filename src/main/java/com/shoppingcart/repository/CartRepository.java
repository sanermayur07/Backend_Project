package com.shoppingcart.repository;

import com.shoppingcart.model.Cart;

import com.shoppingcart.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
    
    Optional<Cart> findByUser_UserId(Long userId);
    
}