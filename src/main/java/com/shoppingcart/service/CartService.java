package com.shoppingcart.service;

import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Cart;
import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.Product;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.CartItemRepository;
import com.shoppingcart.repository.CartRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================	

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    // ==========================================
    // ADD PRODUCT TO CART
    // ==========================================

    public Cart addToCart(
            Long userId,
            Long productId,
            Integer quantity) {

        // Validate that the logged-in user
        // is allowed to use this userId
        validateUserAccess(userId);


        // Validate quantity

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }


        // Find user

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        // Find product

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + productId
                        )
                );


        // Check product stock

        if (product.getQuantity() == null ||
                product.getQuantity() <= 0) {

            throw new IllegalArgumentException(
                    "Product is out of stock"
            );
        }


        // Check requested quantity against stock

        if (quantity > product.getQuantity()) {

            throw new IllegalArgumentException(
                    "Requested quantity is greater than available stock"
            );
        }


        // Find existing cart or create new cart

        Cart cart = cartRepository
                .findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });


        // Check whether product already exists in cart

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(
                                cart,
                                product
                        )
                        .orElse(null);


        if (cartItem != null) {

            // Existing product

            int newQuantity =
                    cartItem.getQuantity() + quantity;


            // Check total quantity against stock

            if (newQuantity > product.getQuantity()) {

                throw new IllegalArgumentException(
                        "Total cart quantity is greater than available stock"
                );
            }


            cartItem.setQuantity(newQuantity);

        } else {

            // New product

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }


        cartItemRepository.save(cartItem);

        return cart;
    }


    // ==========================================
    // UPDATE CART QUANTITY
    // ==========================================

    public CartItem updateCartQuantity(
            Long userId,
            Long cartItemId,
            Integer quantity) {

        // Validate user ownership
        validateUserAccess(userId);


        // Validate quantity

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }


        // Get user's cart

        Cart cart = getCart(userId);


        // Find cart item

        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found with ID: "
                                                + cartItemId
                                )
                        );


        // Make sure cart item belongs
        // to this user's cart

        if (cartItem.getCart() == null ||
                !cartItem.getCart()
                        .getCartId()
                        .equals(cart.getCartId())) {

            throw new ResourceNotFoundException(
                    "Cart item does not belong to this user's cart"
            );
        }


        // Get product

        Product product =
                cartItem.getProduct();


        // Check product stock

        if (product.getQuantity() == null ||
                product.getQuantity() <= 0) {

            throw new IllegalArgumentException(
                    "Product is out of stock"
            );
        }


        // Check requested quantity
        // against available stock

        if (quantity > product.getQuantity()) {

            throw new IllegalArgumentException(
                    "Requested quantity is greater than available stock"
            );
        }


        // Update quantity

        cartItem.setQuantity(quantity);


        // Save updated cart item

        return cartItemRepository.save(cartItem);
    }


    // ==========================================
    // GET USER CART
    // ==========================================

    @Transactional(readOnly = true)
    public Cart getCart(Long userId) {

        // Validate user ownership
        validateUserAccess(userId);


        return cartRepository
                .findByUser_UserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user ID: "
                                        + userId
                        )
                );
    }


    // ==========================================
    // GET CART ITEMS
    // ==========================================

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long userId) {

        // getCart() already validates access
        Cart cart = getCart(userId);

        return cartItemRepository.findByCart(cart);
    }


    // ==========================================
    // REMOVE CART ITEM
    // ==========================================

    public void removeFromCart(
            Long userId,
            Long cartItemId) {

        // Validate user ownership
        validateUserAccess(userId);


        Cart cart = getCart(userId);


        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found with ID: "
                                                + cartItemId
                                )
                        );


        // Check that the cart item belongs
        // to the user's cart

        if (cartItem.getCart() == null ||
                !cartItem.getCart()
                        .getCartId()
                        .equals(cart.getCartId())) {

            throw new ResourceNotFoundException(
                    "Cart item does not belong to this user's cart"
            );
        }


        cartItemRepository.delete(cartItem);
    }


    // ==========================================
    // CLEAR CART
    // ==========================================

    public void clearCart(Long userId) {

        // Validate user ownership
        validateUserAccess(userId);


        Cart cart = getCart(userId);


        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);


        cartItemRepository.deleteAll(cartItems);
    }


    // ==========================================
    // CALCULATE CART TOTAL
    // ==========================================

    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long userId) {

        // getCartItems() already validates access
        List<CartItem> cartItems =
                getCartItems(userId);


        BigDecimal total =
                BigDecimal.ZERO;


        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            BigDecimal itemPrice =
                    product.getPrice();


            BigDecimal itemTotal =
                    itemPrice.multiply(
                            BigDecimal.valueOf(
                                    cartItem.getQuantity()
                            )
                    );


            total = total.add(itemTotal);
        }


        return total;
    }


    // ==========================================
    // VALIDATE USER ACCESS
    // ==========================================

    private void validateUserAccess(Long userId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        // Check authentication

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        // Check whether logged-in user is ADMIN

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );


        /*
         * ADMIN can access any user's cart.
         */
        if (isAdmin) {
            return;
        }


        // Get logged-in user's email

        String loggedInEmail =
                authentication.getName();


        // Find logged-in user

        User loggedInUser =
                userRepository.findByEmail(
                        loggedInEmail
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in user not found"
                        )
                );


        // Compare logged-in user ID
        // with requested user ID

        if (!loggedInUser.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "Access Denied: You are not authorized to access this cart"
            );
        }
    }
}