
package com.studentmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentmanagement.dto.MarkRequest;
import com.studentmanagement.entity.*;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.*;

@Service
public class MarkService {

    private final MarkRepository repository;
    private final StudentRepository students;
    private final CourseRepository courses;

    public MarkService(
            MarkRepository repository,
            StudentRepository students,
            CourseRepository courses) {
        this.repository = repository;
        this.students = students;
        this.courses = courses;
    }

    public List<Mark> getAll() {
        return repository.findAll();
    }

    @Transactional
    public Mark create(MarkRequest request) {
        validateScore(request);
        Mark mark = new Mark();
        apply(mark, request);
        return repository.save(mark);
    }

    @Transactional
    public Mark update(Long id, MarkRequest request) {
        validateScore(request);

        Mark mark = repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Mark not found: " + id));

        apply(mark, request);
        return repository.save(mark);
    }

    @Transactional
    public void delete(Long id) {
        Mark mark = repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Mark not found: " + id));

        repository.delete(mark);
    }

    private void validateScore(MarkRequest request) {
        if (request.score() != null
                && request.maxScore() != null
                && request.score() > request.maxScore()) {
            throw new IllegalArgumentException(
                "Score cannot exceed maximum score");
        }
    }

    private void apply(Mark mark, MarkRequest request) {
        Student student = students.findById(request.studentId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Student not found: " + request.studentId()));

        Course course = courses.findById(request.courseId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Course not found: " + request.courseId()));

        mark.setStudent(student);
        mark.setCourse(course);
        mark.setAssessmentName(request.assessmentName());
        mark.setScore(request.score());
        mark.setMaxScore(request.maxScore());
        mark.setAssessmentDate(request.assessmentDate());
    }

public List<Mark> getMarksByStudentEmail(String email) {
    return repository.findByStudent_Email(email);
}

}
