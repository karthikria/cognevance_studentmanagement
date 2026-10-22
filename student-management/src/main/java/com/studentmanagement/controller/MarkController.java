
package com.studentmanagement.controller;
import org.springframework.security.core.Authentication;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.dto.MarkRequest;
import com.studentmanagement.entity.Mark;
import com.studentmanagement.service.MarkService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/marks")

public class MarkController {

    private final MarkService service;

    public MarkController(MarkService service) {
        this.service = service;
    }

    // GET: Get all marks
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<Mark> getAll() {
        return service.getAll();
    }

    // POST: Create marks
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mark create(
            @Valid @RequestBody MarkRequest request) {
        return service.create(request);
    }

    // PUT: Update marks
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Mark update(
            @PathVariable Long id,
            @Valid @RequestBody MarkRequest request) {
        return service.update(id, request);
    }

    // DELETE: Delete marks (Admin only)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

@GetMapping("/my")
@PreAuthorize("hasRole('STUDENT')")
public List<Mark> getMyMarks(Authentication authentication) {
    return service.getMarksByStudentEmail(authentication.getName());
}

}
