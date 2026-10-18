package com.shoppingcart.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_product_review",
                        columnNames = {"user_id", "product_id"}
                )
        }
)
public class Review {

    // ==========================================
    // REVIEW ID
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;


    // ==========================================
    // RATING
    // ==========================================

    @Column(nullable = false)
    private Integer rating;


    // ==========================================
    // REVIEW TEXT
    // ==========================================

    @Column(length = 1000)
    private String review;


    // ==========================================
    // USER
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    // ==========================================
    // PRODUCT
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Review() {
    }

    public Review(
            Integer rating,
            String review,
            User user,
            Product product) {

        this.rating = rating;
        this.review = review;
        this.user = user;
        this.product = product;
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}