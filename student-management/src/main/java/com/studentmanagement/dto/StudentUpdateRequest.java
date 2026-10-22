package com.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StudentUpdateRequest(
    @NotBlank String studentNumber,
    @NotBlank String name,
    @Email @NotBlank String email,
    String phone,
    String department,
    @Min(1) @Max(8) Integer yearOfStudy
) {}