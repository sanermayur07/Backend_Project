package com.shoppingcart.service;

import com.shoppingcart.dto.LoginRequest;
import com.shoppingcart.dto.RegisterRequest;
import com.shoppingcart.dto.ResetPasswordRequest;
import com.shoppingcart.dto.UpdateUserRequest;
import com.shoppingcart.dto.UserResponse;
import com.shoppingcart.exception.DuplicateResourceException;
import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // ==========================================
    // REGISTER CUSTOMER
    // ==========================================

    public UserResponse registerUser(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        // Check whether email already exists
        if (userRepository.existsByEmail(email)) {

            throw new DuplicateResourceException(
                    "Email is already registered"
            );
        }

        // Create new User entity
        User user = new User();

        // Set basic information
        user.setName(
                request.getName().trim()
        );

        user.setEmail(email);

        // Password must be encrypted
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setPhone(
                request.getPhone()
        );

        user.setAddress(
                request.getAddress()
        );

        /*
         * Public registration always creates
         * a CUSTOMER account.
         */
        user.setRole("CUSTOMER");

        // Save user
        User savedUser =
                userRepository.save(user);

        // Return DTO
        return convertToResponse(savedUser);
    }


    // ==========================================
    // LOGIN
    // ==========================================

    public UserResponse loginUser(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        /*
         * Spring Security already authenticates
         * the username and password.
         *
         * Here we only retrieve the user
         * to create UserResponse.
         */

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return convertToResponse(user);
    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    public UserResponse getUserById(Long userId) {

        /*
         * Security check:
         *
         * CUSTOMER -> only their own profile
         * ADMIN    -> any user's profile
         */
        validateUserAccess(userId);

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with ID: "
                                                + userId
                                )
                        );

        return convertToResponse(user);
    }


    // ==========================================
    // GET CURRENT LOGGED-IN USER
    // ==========================================

    public UserResponse getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String email =
                authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return convertToResponse(user);
    }


    // ==========================================
    // GET ALL USERS
    // ADMIN
    // ==========================================

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // UPDATE USER
    // ==========================================

    public UserResponse updateUser(
            Long userId,
            UpdateUserRequest request) {

        /*
         * Security check:
         *
         * CUSTOMER -> can update only own profile
         * ADMIN    -> can update any user
         */
        validateUserAccess(userId);

        // Find existing user
        User existingUser =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with ID: "
                                                + userId
                                )
                        );

        /*
         * Update only allowed fields.
         *
         * Email, role and password are not changed here.
         */

        existingUser.setName(
                request.getName().trim()
        );

        existingUser.setPhone(
                request.getPhone()
        );

        existingUser.setAddress(
                request.getAddress()
        );

        // Save updated user
        User updatedUser =
                userRepository.save(existingUser);

        return convertToResponse(updatedUser);
    }


    // ==========================================
    // RESET PASSWORD
    // ==========================================

    public void resetPassword(
            ResetPasswordRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No account found with this email"
                                )
                        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }


    // ==========================================
    // DELETE USER
    // ADMIN
    // ==========================================

    public void deleteUser(Long userId) {

        // Check user exists
        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with ID: " + userId
            );
        }

        // Delete user
        userRepository.deleteById(userId);
    }


    // ==========================================
    // GET USER BY EMAIL
    // ==========================================

    public UserResponse getUserByEmail(String email) {

        User user =
                userRepository.findByEmail(
                        email.trim().toLowerCase()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return convertToResponse(user);
    }


    // ==========================================
    // VALIDATE USER ACCESS
    // ==========================================

    private void validateUserAccess(Long userId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        /*
         * Check whether authentication exists.
         */
        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        /*
         * Check whether logged-in user is ADMIN.
         *
         * ADMIN can access any user's profile.
         */
        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        if (isAdmin) {
            return;
        }


        /*
         * If the user is CUSTOMER,
         * find the currently logged-in user.
         */
        String loggedInEmail =
                authentication.getName();

        User loggedInUser =
                userRepository.findByEmail(
                        loggedInEmail
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in user not found"
                        )
                );


        /*
         * Compare logged-in user's ID
         * with requested userId.
         */
        if (!loggedInUser.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to access this user"
            );
        }
    }


    // ==========================================
    // ENTITY → RESPONSE DTO
    // ==========================================

    private UserResponse convertToResponse(
            User user) {

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole()
        );
    }
}