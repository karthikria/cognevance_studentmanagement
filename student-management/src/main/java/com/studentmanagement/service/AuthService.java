
package com.studentmanagement.service;

import java.time.Instant;
import java.util.Locale;

import com.studentmanagement.dto.AuthResponse;
import com.studentmanagement.dto.LoginRequest;
import com.studentmanagement.dto.RegisterRequest;
import com.studentmanagement.entity.AppUser;
import com.studentmanagement.enums.Role;
import com.studentmanagement.repository.AppUserRepository;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository users,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (request.password().length() < 8) {
            throw new IllegalArgumentException(
                "Password must contain at least 8 characters"
            );
        }

        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                "Email already registered"
            );
        }

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPasswordHash(
            passwordEncoder.encode(request.password())
        );

        // Public registration can only create STUDENT accounts.
        user.setRole(Role.STUDENT);
        user.setEnabled(true);

        users.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        AppUser user = users.findByEmailIgnoreCase(email)
            .orElseThrow(() ->
                new BadCredentialsException(
                    "Invalid email or password"
                )
            );

        if (!user.isEnabled()
                || !passwordEncoder.matches(
                    request.password(),
                    user.getPasswordHash())) {

            throw new BadCredentialsException(
                "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(
            user.getEmail(),
            user.getRole().name()
        );

        Instant expiry = Instant.now().plusMillis(
            jwtService.getExpirationMs()
        );

        return new AuthResponse(
            token,
            "Bearer",
            user.getEmail(),
            user.getRole().name(),
            expiry.toString()
        );
    }
}
