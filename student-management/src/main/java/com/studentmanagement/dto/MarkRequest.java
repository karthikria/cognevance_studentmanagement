
package com.studentmanagement.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarkRequest(
    @NotNull Long studentId,
    @NotNull Long courseId,
    @NotBlank String assessmentName,
    @NotNull @DecimalMin("0.0") Double score,
    @NotNull @DecimalMin("0.1") Double maxScore,
    LocalDate assessmentDate
) {}
