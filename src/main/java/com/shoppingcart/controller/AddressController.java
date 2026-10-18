package com.shoppingcart.controller;

import com.shoppingcart.dto.AddressRequest;
import com.shoppingcart.dto.AddressResponse;
import com.shoppingcart.service.AddressService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;


    public AddressController(
            AddressService addressService) {

        this.addressService = addressService;
    }


    // ==========================================
    // ADD ADDRESS
    // ==========================================

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @RequestParam Long userId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.addAddress(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ==========================================
    // GET ALL USER ADDRESSES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses(
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                addressService.getUserAddresses(userId)
        );
    }


    // ==========================================
    // GET ADDRESS BY ID
    // ==========================================

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(
            @PathVariable Long addressId,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                addressService.getAddressById(
                        userId,
                        addressId
                )
        );
    }


    // ==========================================
    // UPDATE ADDRESS
    // ==========================================

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestParam Long userId,
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        userId,
                        addressId,
                        request
                )
        );
    }


    // ==========================================
    // DELETE ADDRESS
    // ==========================================

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId,
            @RequestParam Long userId) {

        addressService.deleteAddress(
                userId,
                addressId
        );

        return ResponseEntity.ok(
                "Address deleted successfully"
        );
    }
}