package com.shoppingcart.dto;

import jakarta.validation.constraints.*;


public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;


    @NotBlank(message = "Card number is required")
    @Pattern(
            regexp = "\\d{16}",
            message = "Card number must contain exactly 16 digits"
    )
    private String cardNumber;


    @NotBlank(message = "Cardholder name is required")
    @Size(
            min = 2,
            max = 100,
            message = "Cardholder name must be between 2 and 100 characters"
    )
    private String cardholderName;


    @NotBlank(message = "Expiry date is required")
    @Pattern(
            regexp = "(0[1-9]|1[0-2])/\\d{2}",
            message = "Expiry date must be in MM/YY format"
    )
    private String expiryDate;


    @NotBlank(message = "CVV is required")
    @Pattern(
            regexp = "\\d{3}",
            message = "CVV must contain exactly 3 digits"
    )
    private String cvv;


    // Constructors

    public PaymentRequest() {
    }

    

    public PaymentRequest(@NotNull(message = "Order ID is required") Long orderId,
			@NotBlank(message = "Card number is required") @Pattern(regexp = "\\d{16}", message = "Card number must contain exactly 16 digits") String cardNumber,
			@NotBlank(message = "Cardholder name is required") @Size(min = 2, max = 100, message = "Cardholder name must be between 2 and 100 characters") String cardholderName,
			@NotBlank(message = "Expiry date is required") @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiry date must be in MM/YY format") String expiryDate,
			@NotBlank(message = "CVV is required") @Pattern(regexp = "\\d{3}", message = "CVV must contain exactly 3 digits") String cvv) {
		super();
		this.orderId = orderId;
		this.cardNumber = cardNumber;
		this.cardholderName = cardholderName;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
	}



	// Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }


    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}