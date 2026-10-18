package com.shoppingcart.dto;

public class ReviewResponse {

    private Long reviewId;

    private Long userId;

    private String userName;

    private Long productId;

    private String productName;

    private Integer rating;

    private String review;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public ReviewResponse() {
    }


    public ReviewResponse(
            Long reviewId,
            Long userId,
            String userName,
            Long productId,
            String productName,
            Integer rating,
            String review) {

        this.reviewId = reviewId;
        this.userId = userId;
        this.userName = userName;
        this.productId = productId;
        this.productName = productName;
        this.rating = rating;
        this.review = review;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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