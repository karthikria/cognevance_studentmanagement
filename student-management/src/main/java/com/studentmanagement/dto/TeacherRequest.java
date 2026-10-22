//
//package com.studentmanagement.dto;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//
//public record TeacherRequest(
//    @NotBlank String employeeNumber,
//    @NotBlank String name,
//    @Email @NotBlank String email,
//    String department,
//    String phone
//) {}

package com.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TeacherRequest(
    @NotBlank String employeeNumber,
    @NotBlank String name,
    @Email @NotBlank String email,
    String department,
    String phone,
    String initialPassword
) {}
