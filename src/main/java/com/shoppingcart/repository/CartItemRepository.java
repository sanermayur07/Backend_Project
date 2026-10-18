package com.shoppingcart.repository;

import com.shoppingcart.model.Cart;

import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(
            Cart cart,
            Product product
    );

    List<CartItem> findByCart(
            Cart cart
    );
    
    List<CartItem> findByCart_CartId(
            Long cartId
    );
    
}