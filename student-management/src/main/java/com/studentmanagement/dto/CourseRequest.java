
package com.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record CourseRequest(
    @NotBlank String courseCode,
    @NotBlank String title,
    String description,
    @Min(1) @Max(10) Integer credits,
    Long teacherId
) {}
