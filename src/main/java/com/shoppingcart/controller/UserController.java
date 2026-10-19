	package com.shoppingcart.controller;

import com.shoppingcart.dto.LoginRequest;
import com.shoppingcart.dto.RegisterRequest;
import com.shoppingcart.dto.ResetPasswordRequest;
import com.shoppingcart.dto.UpdateUserRequest;
import com.shoppingcart.dto.UserResponse;
import com.shoppingcart.service.AuthenticationService;
import com.shoppingcart.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            AuthenticationService authenticationService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }


    // ==========================================
    // REGISTER CUSTOMER
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response =
                userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ==========================================
    // LOGIN
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail().trim().toLowerCase(),
                                    request.getPassword()
                            )
                    );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            HttpSessionSecurityContextRepository securityContextRepository =
                    new HttpSessionSecurityContextRepository();

            securityContextRepository.saveContext(
                    context,
                    httpRequest,
                    httpResponse
            );

            UserResponse response = userService.loginUser(request);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect password");
        }
    }


    // ==========================================
    // GET CURRENT LOGGED-IN USER
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {

        return ResponseEntity.ok(
                userService.getCurrentUser()
        );
    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long userId) {

        UserResponse response =
                userService.getUserById(userId);

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET ALL USERS
    // ADMIN
    // ==========================================

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users =
                userService.getAllUsers();

        return ResponseEntity.ok(users);
    }


    // ==========================================
    // UPDATE USER
    // ==========================================

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse response =
                userService.updateUser(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // DELETE USER
    // ADMIN
    // ==========================================

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }


 // ==========================================
 // RESET PASSWORD
 // ==========================================

 @PostMapping("/reset-password")
 	public ResponseEntity<String> resetPassword(
         @Valid @RequestBody ResetPasswordRequest request) {

     userService.resetPassword(request);

     return ResponseEntity.ok(
             "Password reset successfully"
    	);
 	}
    
    // ==========================================
    // LOGOUT
    // ==========================================

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        authenticationService.logout(
                request,
                response
        );

        return ResponseEntity.ok(
                "Logout successful"
        );
    }
}