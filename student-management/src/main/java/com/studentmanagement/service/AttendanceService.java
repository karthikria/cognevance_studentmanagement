
package com.studentmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentmanagement.dto.AttendanceRequest;
import com.studentmanagement.entity.*;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.*;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;
    private final StudentRepository students;
    private final CourseRepository courses;

    public AttendanceService(
            AttendanceRepository repository,
            StudentRepository students,
            CourseRepository courses) {
        this.repository = repository;
        this.students = students;
        this.courses = courses;
    }

    public List<Attendance> getAll() {
        return repository.findAll();
    }

    @Transactional
    public Attendance create(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        apply(attendance, request);
        return repository.save(attendance);
    }

    @Transactional
    public Attendance update(Long id, AttendanceRequest request) {
        Attendance attendance = repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Attendance not found: " + id));

        apply(attendance, request);
        return repository.save(attendance);
    }

    @Transactional
    public void delete(Long id) {
        Attendance attendance = repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Attendance not found: " + id));

        repository.delete(attendance);
    }

    private void apply(
            Attendance attendance, AttendanceRequest request) {

        Student student = students.findById(request.studentId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Student not found: " + request.studentId()));

        Course course = courses.findById(request.courseId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Course not found: " + request.courseId()));

        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setAttendanceDate(request.attendanceDate());
        attendance.setStatus(request.status());
        attendance.setRemarks(request.remarks());
    }
    public List<Attendance> getAttendanceByStudentEmail(String email) {
        return repository.findByStudent_EmailIgnoreCase(email);
    }
}
