package com.shoppingcart.controller;

import com.shoppingcart.dto.AdminUserResponse;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // ==========================================
    // TEST ADMIN ACCESS
    // ==========================================

    @GetMapping("/test")
    public ResponseEntity<String> adminTest() {

        return ResponseEntity.ok(
                "Admin access is working"
        );
    }


    // ==========================================
    // GET ALL USERS
    // ==========================================

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<AdminUserResponse> response =
                users.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserResponse> getUserById(
            @PathVariable Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + userId
                        )
                );

        return ResponseEntity.ok(
                convertToResponse(user)
        );
    }


    // ==========================================
    // ENTITY → DTO
    // ==========================================

    private AdminUserResponse convertToResponse(User user) {

        return new AdminUserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole()
        );
    }
}