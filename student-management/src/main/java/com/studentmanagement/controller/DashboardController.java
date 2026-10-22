
package com.studentmanagement.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> adminDashboard() {
        return service.adminDashboard();
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public Map<String, Object> teacherDashboard() {
        return service.teacherDashboard();
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public Map<String, Object> studentDashboard() {
        return service.studentDashboard();
    }
}
