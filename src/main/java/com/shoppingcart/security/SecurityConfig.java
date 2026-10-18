package com.shoppingcart.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService) {

        this.customUserDetailsService =
                customUserDetailsService;
    }

    // ==========================================
    // PASSWORD ENCODER
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ==========================================
    // AUTHENTICATION PROVIDER
    // ==========================================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        customUserDetailsService
                );

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // ==========================================
    // AUTHENTICATION MANAGER
    // ==========================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // ==========================================
    // SECURITY FILTER CHAIN
    // ==========================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
        
        		.cors(cors -> {})
                // REST API development
                .csrf(csrf -> csrf.disable())

                // Session authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                .authorizeHttpRequests(auth -> auth

                	    // ==========================================
                	    // PUBLIC USER APIs
                	    // ==========================================

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.POST,
                	            "/api/users/register",
                	            "/api/users/login"
                	    ).permitAll()


                	    
                	    
                	    // ==========================================
                	    // CURRENT USER
                	    // ==========================================

                	    .requestMatchers(
                	            "/api/users/me"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")


                	    // ==========================================
                	    // PRODUCTS - GET
                	    // ==========================================

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.GET,
                	            "/api/products",
                	            "/api/products/**"
                	    ).permitAll()


                	    // ==========================================
                	    // ADMIN PRODUCT MANAGEMENT
                	    // ==========================================

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.POST,
                	            "/api/products"
                	    ).hasRole("ADMIN")

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.PUT,
                	            "/api/products/**"
                	    ).hasRole("ADMIN")

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.DELETE,
                	            "/api/products/**"
                	    ).hasRole("ADMIN")


                	    // ==========================================
                	    // ADMIN USER MANAGEMENT
                	    // ==========================================

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.GET,
                	            "/api/users"
                	    ).hasRole("ADMIN")

                	 // ==========================================
                	 // ADMIN APIs
                	 // ==========================================

                	 .requestMatchers(
                	         "/api/admin/**"
                	 ).hasRole("ADMIN")

                	    // ==========================================
                	    // USER PROFILE
                	    // ==========================================

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.GET,
                	            "/api/users/{userId}"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.PUT,
                	            "/api/users/{userId}"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.DELETE,
                	            "/api/users/{userId}"
                	    ).hasRole("ADMIN")


                	    // ==========================================
                	    // CART APIs
                	    // ==========================================

                	    .requestMatchers(
                	            "/api/cart/**"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")


                	    // ==========================================
                	    // PAYMENT APIs
                	    // ==========================================

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.POST,
                	            "/api/payment/process"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.GET,
                	            "/api/payment/{paymentId}"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")

                	    .requestMatchers(
                	            org.springframework.http.HttpMethod.GET,
                	            "/api/payment/order/{orderId}"
                	    ).hasAnyRole("CUSTOMER", "ADMIN")

                	 // ==========================================
                	 // REVIEWS
                	 // ==========================================

                	 // Anyone can view product reviews
                	 .requestMatchers(
                	         HttpMethod.GET,
                	         "/api/reviews",
                	         "/api/reviews/**"
                	 ).permitAll()

                	 // Customers and admins can create reviews
                	 .requestMatchers(
                	         HttpMethod.POST,
                	         "/api/reviews"
                	 ).hasAnyRole("CUSTOMER", "ADMIN")

                	 // Customers and admins can update reviews
                	 .requestMatchers(
                	         HttpMethod.PUT,
                	         "/api/reviews/**"
                	 ).hasAnyRole("CUSTOMER", "ADMIN")

                	 // Customers and admins can delete reviews
                	 .requestMatchers(
                	         HttpMethod.DELETE,
                	         "/api/reviews/**"
                	 ).hasAnyRole("CUSTOMER", "ADMIN")
                	 
                	// ==========================================
                	// ADDRESS APIs
                	// ==========================================

                	// CUSTOMER and ADMIN can create address
                	.requestMatchers(
                	        org.springframework.http.HttpMethod.POST,
                	        "/api/addresses"
                	).hasAnyRole("CUSTOMER", "ADMIN")

                	// CUSTOMER and ADMIN can view addresses
                	.requestMatchers(
                	        org.springframework.http.HttpMethod.GET,
                	        "/api/addresses",
                	        "/api/addresses/**"
                	).hasAnyRole("CUSTOMER", "ADMIN")

                	// CUSTOMER and ADMIN can update address
                	.requestMatchers(
                	        org.springframework.http.HttpMethod.PUT,
                	        "/api/addresses/**"
                	).hasAnyRole("CUSTOMER", "ADMIN")

                	// CUSTOMER and ADMIN can delete address
                	.requestMatchers(
                	        org.springframework.http.HttpMethod.DELETE,
                	        "/api/addresses/**"
                	).hasAnyRole("CUSTOMER", "ADMIN")
                	
                	//
                	
                	.requestMatchers(HttpMethod.GET, "/api/orders")
                    .hasRole("ADMIN")
                	
                    .requestMatchers(HttpMethod.PUT, "/api/orders/*/status")
                    .hasRole("ADMIN")
                    
                	    // ==========================================
                	    // EVERYTHING ELSE
                	    // ==========================================

                	    .anyRequest().authenticated()
                	);

        return http.build();
    }
}