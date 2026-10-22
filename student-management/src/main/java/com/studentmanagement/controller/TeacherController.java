
package com.studentmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.dto.TeacherRequest;
import com.studentmanagement.entity.Teacher;
import com.studentmanagement.service.TeacherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // GET: Get all teachers
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(
            teacherService.getAll()
        );
    }

    // GET: Get teacher by ID
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            teacherService.getById(id)
        );
    }

    // POST: Create teacher
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(
            @Valid @RequestBody TeacherRequest request) {

        Teacher teacher = teacherService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teacher);
    }

    // PUT: Update teacher
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequest request) {

        Teacher teacher = teacherService.update(id, request);

        return ResponseEntity.ok(teacher);
    }

    // DELETE: Delete teacher
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(
            @PathVariable Long id) {

        teacherService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
