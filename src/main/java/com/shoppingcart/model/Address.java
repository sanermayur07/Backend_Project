package com.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    // ==========================================
    // PRIMARY KEY
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;


    // ==========================================
    // USER
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    @JsonIgnore
    private User user;


    // ==========================================
    // ADDRESS DETAILS
    // ==========================================

    @Column(nullable = false, length = 100)
    private String fullName;


    @Column(nullable = false, length = 15)
    private String phone;


    @Column(nullable = false, length = 255)
    private String addressLine;


    @Column(nullable = false, length = 100)
    private String city;


    @Column(nullable = false, length = 100)
    private String state;


    @Column(nullable = false, length = 10)
    private String postalCode;


    @Column(length = 100)
    private String country = "India";


    // ==========================================
    // ADDRESS TYPE
    // ==========================================

    @Column(length = 20)
    private String addressType = "HOME";


    // ==========================================
    // DEFAULT ADDRESS
    // ==========================================

    @Column(nullable = false)
    private Boolean isDefault = false;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Address() {
    }


    public Address(
            User user,
            String fullName,
            String phone,
            String addressLine,
            String city,
            String state,
            String postalCode,
            String country,
            String addressType,
            Boolean isDefault) {

        this.user = user;
        this.fullName = fullName;
        this.phone = phone;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.addressType = addressType;
        this.isDefault = isDefault;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }


    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}