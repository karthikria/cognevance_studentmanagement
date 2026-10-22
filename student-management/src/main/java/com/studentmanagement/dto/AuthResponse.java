
package com.studentmanagement.dto;

public record AuthResponse(
    String token,
    String tokenType,
    String email,
    String role,
    String expiresAt
) {}
