package com.shoppingcart.service;

import com.shoppingcart.dto.PaymentRequest;
import com.shoppingcart.dto.PaymentResponse;
import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Order;
import com.shoppingcart.model.Payment;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.PaymentRepository;
import com.shoppingcart.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }


    // ==========================================
    // PROCESS PAYMENT
    // ==========================================

    public PaymentResponse processPayment(
            PaymentRequest request) {

        // ------------------------------------------
        // 1. VALIDATE CARD NUMBER
        // ------------------------------------------

        if (request.getCardNumber() == null ||
                request.getCardNumber().trim().length() < 4) {

            throw new IllegalArgumentException(
                    "Invalid card number"
            );
        }


        // ------------------------------------------
        // 2. FIND ORDER
        // ------------------------------------------

        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: "
                                        + request.getOrderId()
                        )
                );


        // ------------------------------------------
        // 3. CHECK ORDER OWNERSHIP
        // ------------------------------------------

        validateUserAccess(
                order.getUser().getUserId()
        );


        // ------------------------------------------
        // 4. CHECK DUPLICATE PAYMENT
        // ------------------------------------------

        if (paymentRepository.existsByOrder(order)) {

            throw new IllegalArgumentException(
                    "Payment already exists for this order"
            );
        }


        // Validate card number

        String cardNumber = request.getCardNumber();

        if (cardNumber == null ||
                !cardNumber.matches("\\d{16}")) {

            throw new IllegalArgumentException(
                    "Card number must contain exactly 16 digits"
            );
        }


        // ------------------------------------------
        // 5. GET LAST 4 DIGITS
        // ------------------------------------------

        String lastFourDigits =
                cardNumber.substring(
                        cardNumber.length() - 4
                );


        // ------------------------------------------
        // 6. CREATE PAYMENT ENTITY
        // ------------------------------------------

        Payment payment = new Payment();

        payment.setOrder(order);


        // Payment amount comes from the order.
        // Customer should not decide the payment amount.

        payment.setAmount(
                order.getTotalAmount()
        );


        // ------------------------------------------
        // 7. PAYMENT METHOD
        // ------------------------------------------

        payment.setPaymentMethod("CARD");


        // ------------------------------------------
        // 8. PAYMENT STATUS
        // ------------------------------------------

        /*
         * This is a simulated payment system
         * for the academic project.
         *
         * In a real application this would be
         * confirmed by a payment gateway.
         */

        payment.setPaymentStatus("SUCCESS");


        // ------------------------------------------
        // 9. STORE ONLY LAST 4 DIGITS
        // ------------------------------------------

        payment.setCardLastFour(
                lastFourDigits
        );


        // ------------------------------------------
        // 10. CARDHOLDER NAME
        // ------------------------------------------

        payment.setCardholderName(
                request.getCardholderName()
        );


        // ------------------------------------------
        // 11. GENERATE TRANSACTION ID
        // ------------------------------------------

        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
        );


        // ------------------------------------------
        // 12. PAYMENT DATE
        // ------------------------------------------

        payment.setPaymentDate(
                LocalDateTime.now()
        );


        // ------------------------------------------
        // 13. SAVE PAYMENT
        // ------------------------------------------

        Payment savedPayment =
                paymentRepository.save(payment);


        // ------------------------------------------
        // 14. UPDATE ORDER STATUS
        // ------------------------------------------

        order.setStatus("PLACED");

        orderRepository.save(order);


        // ------------------------------------------
        // 15. RETURN PAYMENT RESPONSE
        // ------------------------------------------

        return convertToResponse(savedPayment);
    }


    // ==========================================
    // GET PAYMENT BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(
            Long paymentId) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found with ID: "
                                                + paymentId
                                )
                        );


        // ------------------------------------------
        // CHECK PAYMENT OWNERSHIP
        // ------------------------------------------

        validateUserAccess(
                payment.getOrder()
                        .getUser()
                        .getUserId()
        );


        return convertToResponse(payment);
    }


    // ==========================================
    // GET PAYMENT BY ORDER ID
    // ==========================================

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long orderId) {

        // ------------------------------------------
        // FIND ORDER
        // ------------------------------------------

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with ID: "
                                                + orderId
                                )
                        );


        // ------------------------------------------
        // CHECK ORDER OWNERSHIP
        // ------------------------------------------

        validateUserAccess(
                order.getUser().getUserId()
        );


        // ------------------------------------------
        // FIND PAYMENT
        // ------------------------------------------

        Payment payment =
                paymentRepository.findByOrder(order)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for order ID: "
                                                + orderId
                                )
                        );


        return convertToResponse(payment);
    }


    // ==========================================
    // PAYMENT OWNERSHIP SECURITY
    // ==========================================

    private void validateUserAccess(Long userId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        // ------------------------------------------
        // CHECK AUTHENTICATION
        // ------------------------------------------

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        // ------------------------------------------
        // CHECK ADMIN ROLE
        // ------------------------------------------

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );


        // ADMIN CAN ACCESS ANY PAYMENT
        if (isAdmin) {
            return;
        }


        // ------------------------------------------
        // GET LOGGED-IN USER
        // ------------------------------------------

        String loggedInEmail =
                authentication.getName();


        User loggedInUser =
                userRepository
                        .findByEmail(loggedInEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Logged-in user not found"
                                )
                        );


        // ------------------------------------------
        // CHECK OWNERSHIP
        // ------------------------------------------

        if (!loggedInUser.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "Access Denied: You are not authorized to access this payment"
            );
        }
    }


    // ==========================================
    // ENTITY → DTO
    // ==========================================

    private PaymentResponse convertToResponse(
            Payment payment) {

        return new PaymentResponse(

                payment.getPaymentId(),

                payment.getOrder().getOrderId(),

                payment.getAmount(),

                payment.getPaymentMethod(),

                payment.getPaymentStatus(),

                payment.getCardLastFour(),

                payment.getCardholderName(),

                payment.getTransactionId(),

                payment.getPaymentDate()
        );
    }
}