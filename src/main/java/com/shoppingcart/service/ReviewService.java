package com.shoppingcart.service;

import com.shoppingcart.dto.ReviewRequest;
import com.shoppingcart.dto.ReviewResponse;
import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Product;
import com.shoppingcart.model.Review;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.repository.ReviewRepository;
import com.shoppingcart.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    // ==========================================
    // ADD REVIEW
    // ==========================================

    public ReviewResponse addReview(
            ReviewRequest request) {

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: "
                                        + request.getUserId()
                        )
                );


        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: "
                                        + request.getProductId()
                        )
                );


        // ==========================================
        // CHECK DUPLICATE REVIEW
        // ==========================================

        if (reviewRepository.existsByUserAndProduct(
                user,
                product)) {

            throw new IllegalArgumentException(
                    "You have already reviewed this product"
            );
        }


        // ==========================================
        // CREATE REVIEW
        // ==========================================

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setReview(request.getReview());


        Review savedReview =
                reviewRepository.save(review);


        return convertToResponse(savedReview);
    }


    // ==========================================
    // GET REVIEW BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(
            Long reviewId) {

        Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found with ID: "
                                                + reviewId
                                )
                        );

        return convertToResponse(review);
    }


    // ==========================================
    // GET ALL REVIEWS FOR PRODUCT
    // ==========================================

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProduct(
            Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with ID: "
                                                + productId
                                )
                        );


        return reviewRepository
                .findByProduct(product)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // GET ALL REVIEWS BY USER
    // ==========================================

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUser(
            Long userId) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with ID: "
                                                + userId
                                )
                        );


        return reviewRepository
                .findByUser(user)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // UPDATE REVIEW
    // ==========================================

    public ReviewResponse updateReview(
            Long reviewId,
            Long userId,
            ReviewRequest request) {

        Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found with ID: "
                                                + reviewId
                                )
                        );


        // Make sure review belongs to this user

        if (!review.getUser()
                .getUserId()
                .equals(userId)) {

            throw new IllegalArgumentException(
                    "You can only update your own review"
            );
        }


        review.setRating(request.getRating());
        review.setReview(request.getReview());


        Review updatedReview =
                reviewRepository.save(review);


        return convertToResponse(updatedReview);
    }


    // ==========================================
    // DELETE REVIEW
    // ==========================================

    public void deleteReview(
            Long reviewId,
            Long userId) {

        Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found with ID: "
                                                + reviewId
                                )
                        );


        // Make sure review belongs to this user

        if (!review.getUser()
                .getUserId()
                .equals(userId)) {

            throw new IllegalArgumentException(
                    "You can only delete your own review"
            );
        }


        reviewRepository.delete(review);
    }


    // ==========================================
    // ENTITY → DTO
    // ==========================================

    private ReviewResponse convertToResponse(
            Review review) {

        return new ReviewResponse(

                review.getReviewId(),

                review.getUser().getUserId(),

                review.getUser().getName(),

                review.getProduct().getProductId(),

                review.getProduct().getName(),

                review.getRating(),

                review.getReview()
        );
    }
}