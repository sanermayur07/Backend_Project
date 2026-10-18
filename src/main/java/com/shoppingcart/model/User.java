package com.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    // ==========================================
    // PRIMARY KEY
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    // ==========================================
    // NAME
    // ==========================================

    @Column(
            nullable = false,
            length = 100
    )
    private String name;


    // ==========================================
    // EMAIL
    // ==========================================

    @Column(
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;


    // ==========================================
    // PASSWORD
    // ==========================================

    @JsonIgnore
    @Column(
            nullable = false,
            length = 255
    )
    private String password;


    // ==========================================
    // PHONE
    // ==========================================

    @Column(length = 15)
    private String phone;


    // ==========================================
    // ADDRESS
    // ==========================================

    @Column(length = 255)
    private String address;


    // ==========================================
    // ROLE
    // ==========================================

    @Column(
            nullable = false,
            length = 20
    )
    private String role = "CUSTOMER";


    // ==========================================
    // CART
    // ==========================================

    /*
     * One User has one Cart.
     *
     * Cart.java is the owning side because
     * Cart contains:
     *
     * @JoinColumn(name = "user_id")
     *
     * Therefore User uses mappedBy = "user".
     */

    @OneToOne(
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Cart cart;


    // ==========================================
    // ORDERS
    // ==========================================

    /*
     * One User can have many Orders.
     *
     * Order.java contains:
     *
     * @ManyToOne
     * @JoinColumn(name = "user_id")
     */

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

 // ==========================================
 // ADDRESSES
 // ==========================================

 @JsonIgnore
 @OneToMany(
         mappedBy = "user",
         cascade = CascadeType.ALL,
         orphanRemoval = true
 )
 	private List<Address> addresses = new ArrayList<>();
    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public User() {
    }


    public User(
            String name,
            String email,
            String password,
            String phone,
            String address,
            String role) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    // ==========================================
    // HELPER METHODS
    // ==========================================

    public void setCartRelationship(Cart cart) {

        this.cart = cart;

        if (cart != null && cart.getUser() != this) {
            cart.setUser(this);
        }
    }


    public void addOrder(Order order) {

        orders.add(order);

        order.setUser(this);
    }


    public void removeOrder(Order order) {

        orders.remove(order);

        order.setUser(null);
    }
    
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}