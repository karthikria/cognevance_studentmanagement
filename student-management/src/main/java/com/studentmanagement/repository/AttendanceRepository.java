
package com.studentmanagement.repository;

import com.studentmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findByStudent_EmailIgnoreCase(String email);

    List<Attendance> findByCourseId(Long courseId);

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
}
