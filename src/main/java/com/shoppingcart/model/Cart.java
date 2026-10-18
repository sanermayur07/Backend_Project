package com.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;


    // ==========================================
    // USER
    // ==========================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;


    // ==========================================
    // CART ITEMS
    // ==========================================

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }


    // ==========================================
    // HELPER METHODS
    // ==========================================

    public void addCartItem(CartItem cartItem) {

        cartItems.add(cartItem);

        cartItem.setCart(this);
    }


    public void removeCartItem(CartItem cartItem) {

        cartItems.remove(cartItem);

        cartItem.setCart(null);
    }
}