
package com.shoppingcart.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthenticationService(
            AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
    }

    // ==========================================
    // AUTHENTICATE USER
    // ==========================================

    public Authentication authenticate(
            String email,
            String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Check whether the current session is already authenticated
        Authentication currentAuthentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (currentAuthentication != null
                && currentAuthentication.isAuthenticated()
                && !(currentAuthentication instanceof AnonymousAuthenticationToken)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You are already logged in. Please log out before logging in with another account."
            );
        }

        // Authenticate the supplied credentials
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email.trim().toLowerCase(),
                                password
                        )
                );

        // Create a new security context
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        // Save the security context into the HTTP session
        securityContextRepository.saveContext(
                context,
                request,
                response
        );

        return authentication;
    }

    // ==========================================
    // LOGOUT
    // ==========================================

    public void logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }
}