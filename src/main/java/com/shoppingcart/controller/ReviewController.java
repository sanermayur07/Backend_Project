package com.shoppingcart.controller;

import com.shoppingcart.dto.ReviewRequest;
import com.shoppingcart.dto.ReviewResponse;
import com.shoppingcart.service.ReviewService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public ReviewController(
            ReviewService reviewService) {

        this.reviewService = reviewService;
    }


    // ==========================================
    // ADD REVIEW
    // ==========================================

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response =
                reviewService.addReview(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ==========================================
    // GET REVIEW BY ID
    // ==========================================

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(
            @PathVariable Long reviewId) {

        return ResponseEntity.ok(
                reviewService.getReviewById(reviewId)
        );
    }


    // ==========================================
    // GET REVIEWS BY PRODUCT
    // ==========================================

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByProduct(productId)
        );
    }


    // ==========================================
    // GET REVIEWS BY USER
    // ==========================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByUser(userId)
        );
    }


    // ==========================================
    // UPDATE REVIEW
    // ==========================================

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId,
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response =
                reviewService.updateReview(
                        reviewId,
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // DELETE REVIEW
    // ==========================================

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId) {

        reviewService.deleteReview(
                reviewId,
                userId
        );

        return ResponseEntity.ok(
                "Review deleted successfully"
        );
    }
}