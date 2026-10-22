
package com.studentmanagement.controller;
import com.studentmanagement.dto.EnrollmentRequest;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.entity.Enrollment;
import com.studentmanagement.service.EnrollmentService;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // GET: Get all enrollments
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAll();
    }

    // POST: Enroll a student in a course

@PostMapping
@ResponseStatus(HttpStatus.CREATED)
@PreAuthorize("hasRole('ADMIN')")
public Enrollment enrollStudent(
        @RequestBody EnrollmentRequest request) {

    return enrollmentService.enroll(
        request.studentId(),
        request.courseId()
    );
}


    // DELETE: Delete an enrollment
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.delete(id);
    }
}
