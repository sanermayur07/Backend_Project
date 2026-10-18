package com.shoppingcart.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(
        min = 2,
        max = 150,
        message = "Product name must be between 2 and 150 characters"
    )
    private String name;


    @NotBlank(message = "Product description is required")
    @Size(
        max = 1000,
        message = "Description cannot exceed 1000 characters"
    )
    private String description;


    @NotNull(message = "Price is required")
    @DecimalMin(
        value = "0.01",
        message = "Price must be greater than zero"
    )
    private BigDecimal price;


    @NotBlank(message = "Category is required")
    private String category;


    @NotNull(message = "Quantity is required")
    @Min(
        value = 0,
        message = "Quantity cannot be negative"
    )
    private Integer quantity;


    private String image;


    public ProductRequest() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}