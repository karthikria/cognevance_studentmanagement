
package com.studentmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.dto.CourseRequest;
import com.studentmanagement.entity.Course;
import com.studentmanagement.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    // GET all courses
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<Course> getAll() {
        return service.getAllCourses();
    }

    // GET course by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public Course getById(@PathVariable Long id) {
        return service.getCourseById(id);
    }

    // POST create course
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Course create(
            @Valid @RequestBody CourseRequest request) {
        return service.createCourse(request);
    }

    // PUT update course
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Course update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        return service.updateCourse(id, request);
    }

    // DELETE course
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteCourse(id);
    }
}
