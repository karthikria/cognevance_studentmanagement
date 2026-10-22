
package com.studentmanagement.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.studentmanagement.repository.*;

@Service
public class DashboardService {

    private final StudentRepository students;
    private final TeacherRepository teachers;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;
    private final AttendanceRepository attendance;
    private final MarkRepository marks;

    public DashboardService(
            StudentRepository students,
            TeacherRepository teachers,
            CourseRepository courses,
            EnrollmentRepository enrollments,
            AttendanceRepository attendance,
            MarkRepository marks) {
        this.students = students;
        this.teachers = teachers;
        this.courses = courses;
        this.enrollments = enrollments;
        this.attendance = attendance;
        this.marks = marks;
    }

    public Map<String, Object> adminDashboard() {
        return Map.of(
            "totalStudents", students.count(),
            "totalTeachers", teachers.count(),
            "totalCourses", courses.count(),
            "totalEnrollments", enrollments.count(),
            "totalAttendanceRecords", attendance.count(),
            "totalMarksRecords", marks.count()
        );
    }

    public Map<String, Object> teacherDashboard() {
        return Map.of(
            "totalStudents", students.count(),
            "totalCourses", courses.count(),
            "totalAttendanceRecords", attendance.count(),
            "totalMarksRecords", marks.count()
        );
    }

    public Map<String, Object> studentDashboard() {
        return Map.of(
            "message", "Student dashboard authenticated",
            "note", "Personal academic statistics are not yet implemented"
        );
    }
}
