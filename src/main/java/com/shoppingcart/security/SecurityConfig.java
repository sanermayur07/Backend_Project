
package com.shoppingcart.security;

import com.shoppingcart.security.CustomUserDetailsService;
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
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Authentication manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Use the CorsConfigurationSource from CorsConfig
            .cors(cors -> {})

            // Keep disabled only if your API uses a non-CSRF-token approach.
            // See the session/CSRF note below.
            .csrf(csrf -> csrf.disable())

            // Preserve session-based authentication
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            .authenticationProvider(authenticationProvider())

            .authorizeHttpRequests(auth -> auth

                // Public registration and login
                .requestMatchers(HttpMethod.POST,
                        "/api/users/register",
                        "/api/users/login"
                ).permitAll()

                // Public product browsing
                .requestMatchers(HttpMethod.GET,
                        "/api/products",
                        "/api/products/**"
                ).permitAll()

                // Public product reviews
                .requestMatchers(HttpMethod.GET,
                        "/api/reviews",
                        "/api/reviews/**"
                ).permitAll()

                // Current authenticated user
                .requestMatchers("/api/users/me")
                .hasAnyRole("CUSTOMER", "ADMIN")

                // Admin product management
                .requestMatchers(HttpMethod.POST, "/api/products")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/products/**")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                .hasRole("ADMIN")

                // Admin user management
                .requestMatchers(HttpMethod.GET, "/api/users")
                .hasRole("ADMIN")

                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")

                // User profile endpoints
                .requestMatchers(HttpMethod.GET, "/api/users/{userId}")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/users/{userId}")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}")
                .hasRole("ADMIN")

                // Cart
                .requestMatchers("/api/cart/**")
                .hasAnyRole("CUSTOMER", "ADMIN")

                // Payment
                .requestMatchers(HttpMethod.POST, "/api/payment/process")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.GET,
                        "/api/payment/{paymentId}",
                        "/api/payment/order/{orderId}"
                ).hasAnyRole("CUSTOMER", "ADMIN")

                // Review creation, update, and deletion
                .requestMatchers(HttpMethod.POST, "/api/reviews")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/reviews/**")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/reviews/**")
                .hasAnyRole("CUSTOMER", "ADMIN")

                // Addresses
                .requestMatchers(HttpMethod.POST, "/api/addresses")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.GET,
                        "/api/addresses",
                        "/api/addresses/**"
                ).hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/addresses/**")
                .hasAnyRole("CUSTOMER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/addresses/**")
                .hasAnyRole("CUSTOMER", "ADMIN")

                // Admin orders
                .requestMatchers(HttpMethod.GET, "/api/orders")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/orders/*/status")
                .hasRole("ADMIN")

                // All other requests require authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
