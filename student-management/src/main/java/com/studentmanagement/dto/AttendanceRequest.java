
package com.studentmanagement.dto;

import com.studentmanagement.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AttendanceRequest(
    @NotNull Long studentId,
    @NotNull Long courseId,
    @NotNull LocalDate attendanceDate,
    @NotNull AttendanceStatus status,
    String remarks
) {
}
