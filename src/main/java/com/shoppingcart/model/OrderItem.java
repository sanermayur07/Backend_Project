package com.shoppingcart.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    // ==========================================
    // PRIMARY KEY
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;


    // ==========================================
    // ORDER
    // ==========================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;


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
    // QUANTITY
    // ==========================================

    @Column(nullable = false)
    private Integer quantity;


    // ==========================================
    // PRICE AT THE TIME OF ORDER
    // ==========================================

    @Column(
            name = "price",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal price;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public OrderItem() {
    }


    public OrderItem(
            Order order,
            Product product,
            Integer quantity,
            BigDecimal price) {

        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}