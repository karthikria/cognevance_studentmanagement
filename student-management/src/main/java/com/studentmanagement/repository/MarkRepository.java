

package com.studentmanagement.repository;

import com.studentmanagement.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    List<Mark> findByStudent_Email(String email);
}

