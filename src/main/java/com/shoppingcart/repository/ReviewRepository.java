package com.shoppingcart.repository;

import com.shoppingcart.model.Product;
import com.shoppingcart.model.Review;
import com.shoppingcart.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    // Check whether this user has already reviewed this product
    boolean existsByUserAndProduct(
            User user,
            Product product
    );


    // Find a particular user's review for a product
    Optional<Review> findByUserAndProduct(
            User user,
            Product product
    );


    // Get all reviews for a product
    List<Review> findByProduct(
            Product product
    );


    // Get all reviews written by a user
    List<Review> findByUser(
            User user
    );
}