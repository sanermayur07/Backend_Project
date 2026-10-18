package com.shoppingcart.dto;

public class AdminUserResponse {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;

    public AdminUserResponse() {
    }

    public AdminUserResponse(
            Long userId,
            String name,
            String email,
            String phone,
            String address,
            String role) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }
}