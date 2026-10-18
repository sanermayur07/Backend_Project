package com.shoppingcart.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payment_order",
                        columnNames = "order_id"
                ),
                @UniqueConstraint(
                        name = "uk_payment_transaction_id",
                        columnNames = "transaction_id"
                )
        }
)
public class Payment {

    // ==========================================
    // PAYMENT ID
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;


    // ==========================================
    // ORDER
    // ==========================================

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;


    // ==========================================
    // PAYMENT AMOUNT
    // ==========================================

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;


    // ==========================================
    // PAYMENT METHOD
    // ==========================================

    @Column(
            name = "payment_method",
            nullable = false,
            length = 30
    )
    private String paymentMethod;


    // ==========================================
    // PAYMENT STATUS
    // ==========================================

    @Column(
            name = "payment_status",
            nullable = false,
            length = 30
    )
    private String paymentStatus;


    // ==========================================
    // CARD LAST FOUR DIGITS
    // ==========================================

    /*
     * Only the last 4 digits are stored.
     *
     * Example:
     *
     * Full card number:
     * 4111111111111111
     *
     * Stored:
     * 1111
     *
     * NEVER store the complete card number or CVV.
     */

    @Column(
            name = "card_last_four",
            length = 4
    )
    private String cardLastFour;


    // ==========================================
    // CARDHOLDER NAME
    // ==========================================

    @Column(
            name = "cardholder_name",
            length = 100
    )
    private String cardholderName;


    // ==========================================
    // TRANSACTION ID
    // ==========================================

    @Column(
            name = "transaction_id",
            nullable = false,
            length = 100
    )
    private String transactionId;


    // ==========================================
    // PAYMENT DATE
    // ==========================================

    @Column(
            name = "payment_date",
            nullable = false
    )
    private LocalDateTime paymentDate;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Payment() {
    }


    public Payment(
            Order order,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus,
            String cardLastFour,
            String cardholderName,
            String transactionId,
            LocalDateTime paymentDate) {

        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.cardLastFour = cardLastFour;
        this.cardholderName = cardholderName;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }


    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}