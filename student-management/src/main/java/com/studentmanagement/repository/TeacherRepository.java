
package com.studentmanagement.repository;

import com.studentmanagement.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository
        extends JpaRepository<Teacher, Long> {
	boolean existsByEmailIgnoreCase(String email);
}
