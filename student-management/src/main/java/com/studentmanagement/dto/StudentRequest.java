
package com.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentRequest(
    @NotBlank String studentNumber,
    @NotBlank String name,
    @Email @NotBlank String email,
    String phone,
    String department,
    @Min(1) @Max(8) Integer yearOfStudy,

    @NotBlank
    @Size(min = 8, max = 72)
    String initialPassword
) {}
