
package com.studentmanagement.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.dto.AttendanceRequest;
import com.studentmanagement.entity.Attendance;
import com.studentmanagement.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    // Admin and Teacher: view all attendance
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<Attendance> getAll() {
        return service.getAll();
    }

    // Student: view only their own attendance
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Attendance> getMyAttendance(Principal principal) {
        return service.getAttendanceByStudentEmail(
            principal.getName()
        );
    }

    // Admin and Teacher: create attendance
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Attendance create(
            @Valid @RequestBody AttendanceRequest request) {
        return service.create(request);
    }

    // Admin and Teacher: update attendance
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Attendance update(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRequest request) {
        return service.update(id, request);
    }

    // Admin only: delete attendance
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
