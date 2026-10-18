package com.shoppingcart.service;

import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Cart;
import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import com.shoppingcart.model.Product;
import com.shoppingcart.model.User;

import com.shoppingcart.repository.CartItemRepository;
import com.shoppingcart.repository.CartRepository;
import com.shoppingcart.repository.OrderItemRepository;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }


    // ==========================================
    // CHECKOUT
    // ==========================================

    public Order checkout(Long userId) {

        validateUserAccess(userId);

        // ------------------------------------------
        // 1. Find User
        // ------------------------------------------

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        // ------------------------------------------
        // 2. Find Cart
        // ------------------------------------------

        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user ID: " + userId
                        )
                );


        // ------------------------------------------
        // 3. Get Cart Items
        // ------------------------------------------

        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);


        // ------------------------------------------
        // 4. Check Empty Cart
        // ------------------------------------------

        if (cartItems.isEmpty()) {

            throw new IllegalArgumentException(
                    "Cannot checkout because cart is empty"
            );
        }


        // ------------------------------------------
        // 5. Create Order
        // ------------------------------------------

        Order order = new Order();

        order.setUser(user);

        order.setStatus("PENDING");


        BigDecimal totalAmount =
                BigDecimal.ZERO;


        // ------------------------------------------
        // 6. Convert Cart Items to Order Items
        // ------------------------------------------

        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            Integer cartQuantity =
                    cartItem.getQuantity();


            // --------------------------------------
            // Validate product
            // --------------------------------------

            if (product == null) {

                throw new IllegalArgumentException(
                        "Product not found in cart"
                );
            }


            // --------------------------------------
            // Validate quantity
            // --------------------------------------

            if (cartQuantity == null ||
                    cartQuantity <= 0) {

                throw new IllegalArgumentException(
                        "Invalid cart quantity"
                );
            }


            // --------------------------------------
            // Check stock
            // --------------------------------------

            if (product.getQuantity() == null ||
                    product.getQuantity() < cartQuantity) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }


            // --------------------------------------
            // Get product price
            // --------------------------------------

            BigDecimal price =
                    product.getPrice();


            if (price == null) {

                throw new IllegalArgumentException(
                        "Product price is not available for: "
                                + product.getName()
                );
            }


            // --------------------------------------
            // Calculate item total
            // --------------------------------------

            BigDecimal itemTotal =
                    price.multiply(
                            BigDecimal.valueOf(
                                    cartQuantity
                            )
                    );


            totalAmount =
                    totalAmount.add(itemTotal);


            // --------------------------------------
            // Reduce product stock
            // --------------------------------------

            product.setQuantity(
                    product.getQuantity()
                            - cartQuantity
            );


            // --------------------------------------
            // Create OrderItem
            // --------------------------------------

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setProduct(product);

            orderItem.setQuantity(cartQuantity);

            orderItem.setPrice(price);


            // --------------------------------------
            // Add OrderItem to Order
            // --------------------------------------

            order.addOrderItem(orderItem);
        }


        // ------------------------------------------
        // 7. Set Order Total
        // ------------------------------------------

        order.setTotalAmount(totalAmount);


        // ------------------------------------------
        // 8. Save Order
        // ------------------------------------------

        Order savedOrder =
                orderRepository.save(order);


        // ------------------------------------------
        // 9. Save Order Items
        // ------------------------------------------

        /*
         * Because Order has:
         *
         * cascade = CascadeType.ALL
         *
         * orderRepository.save(order) will normally
         * save the OrderItems as well.
         *
         * We don't need to call save() again.
         */


        // ------------------------------------------
        // 10. Clear Cart
        // ------------------------------------------

        cartItemRepository.deleteAll(cartItems);


        // ------------------------------------------
        // 11. Return Order
        // ------------------------------------------

        return savedOrder;
    }


    // ==========================================
    // GET ORDER BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with ID: "
                                                + orderId
                                )
                        );

        validateUserAccess(
                order.getUser().getUserId()
        );

        return order;
    }


    // ==========================================
    // GET USER ORDERS
    // ==========================================

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {

        validateUserAccess(userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: "
                                        + userId
                        )
                );


        return orderRepository.findByUser(user);
    }


    // ==========================================
    // GET ALL ORDERS
    // ADMIN
    // ==========================================

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    // ==========================================
    // UPDATE ORDER STATUS
    // ADMIN
    // ==========================================

    public Order updateOrderStatus(
            Long orderId,
            String status) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with ID: "
                                                + orderId
                                )
                        );


        if (status == null ||
                status.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Order status cannot be empty"
            );
        }


        order.setStatus(
                status.trim().toUpperCase()
        );


        return orderRepository.save(order);
    }


    // ==========================================
    // VALIDATE USER ACCESS
    // ==========================================

    private void validateUserAccess(Long userId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );


        if (isAdmin) {
            return;
        }


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


        if (!loggedInUser.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "Access Denied: You are not authorized to access this order"
            );
        }
    }
}