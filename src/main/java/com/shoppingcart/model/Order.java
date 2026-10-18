package com.shoppingcart.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    // ==========================================
    // ORDER ID
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;


    // ==========================================
    // USER
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    // ==========================================
    // TOTAL AMOUNT
    // ==========================================

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal totalAmount;


    // ==========================================
    // ORDER STATUS
    // ==========================================

    @Column(
            nullable = false,
            length = 30
    )
    private String status = "PENDING";


    // ==========================================
    // ORDER DATE
    // ==========================================

    @Column(
            nullable = false
    )
    private LocalDateTime orderDate;


    // ==========================================
    // ORDER ITEMS
    // ==========================================

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();


    // ==========================================
    // PAYMENT
    // ==========================================

    /*
     * One order can have one payment.
     *
     * Payment.java is the owning side because
     * Payment contains:
     *
     * @JoinColumn(name = "order_id")
     *
     * Therefore this side uses mappedBy = "order".
     */

    @OneToOne(
            mappedBy = "order",
            fetch = FetchType.LAZY
    )
    private Payment payment;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Order() {
        this.orderDate = LocalDateTime.now();
    }


    public Order(
            User user,
            BigDecimal totalAmount,
            String status) {

        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = LocalDateTime.now();
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }


    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }


    // ==========================================
    // HELPER METHODS
    // ==========================================

    public void addOrderItem(OrderItem orderItem) {

        orderItems.add(orderItem);

        orderItem.setOrder(this);
    }


    public void removeOrderItem(OrderItem orderItem) {

        orderItems.remove(orderItem);

        orderItem.setOrder(null);
    }
}