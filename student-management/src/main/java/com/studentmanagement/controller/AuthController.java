
package com.studentmanagement.controller;

import java.util.Map;

import com.studentmanagement.dto.AuthResponse;
import com.studentmanagement.dto.LoginRequest;
import com.studentmanagement.dto.RegisterRequest;
import com.studentmanagement.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return Map.of(
            "message",
            "Student account created successfully"
        );
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }


@GetMapping("/me")
public Map<String, String> currentUser(
        Authentication authentication) {

    String role = authentication.getAuthorities()
        .stream()
        .map(authority -> authority.getAuthority())
        .filter(authority -> authority.startsWith("ROLE_"))
        .map(authority -> authority.substring(5))
        .findFirst()
        .orElse("UNKNOWN");

    return Map.of(
        "email", authentication.getName(),
        "role", role
    );
}

}
