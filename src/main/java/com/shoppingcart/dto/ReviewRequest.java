package com.shoppingcart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Integer rating;

    @Size(
            max = 1000,
            message = "Review cannot exceed 1000 characters"
    )
    private String review;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public ReviewRequest() {
    }


    public ReviewRequest(
            Long userId,
            Long productId,
            Integer rating,
            String review) {

        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.review = review;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}