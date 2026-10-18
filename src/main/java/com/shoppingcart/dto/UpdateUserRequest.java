package com.shoppingcart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(
        min = 3,
        max = 100,
        message = "Name must be between 3 and 100 characters"
    )
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Enter a valid 10 digit Indian phone number"
    )
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(
        max = 255,
        message = "Address cannot exceed 255 characters"
    )
    private String address;


    public UpdateUserRequest() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}