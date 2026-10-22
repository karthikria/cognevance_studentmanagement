
package com.studentmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentmanagement.entity.*;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.*;

@Service
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final StudentRepository students;
    private final CourseRepository courses;

    public EnrollmentService(
            EnrollmentRepository repository,
            StudentRepository students,
            CourseRepository courses) {
        this.repository = repository;
        this.students = students;
        this.courses = courses;
    }

    public List<Enrollment> getAll() {
        return repository.findAll();
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {

        if (repository.existsByStudentIdAndCourseId(
                studentId, courseId)) {
            throw new IllegalArgumentException(
                "Student is already enrolled in this course");
        }

        Student student = students.findById(studentId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Student not found: " + studentId));

        Course course = courses.findById(courseId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Course not found: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(java.time.LocalDate.now());

        return repository.save(enrollment);
    }

    @Transactional
    public void delete(Long id) {
        Enrollment enrollment = repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Enrollment not found: " + id));

        repository.delete(enrollment);
    }
}
