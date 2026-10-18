package com.shoppingcart.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer quantity;
    private String image;


    public ProductResponse() {
    }


    public ProductResponse(
            Long productId,
            String name,
            String description,
            BigDecimal price,
            String category,
            Integer quantity,
            String image) {

        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.image = image;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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