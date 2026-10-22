//
//package com.studentmanagement.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.studentmanagement.dto.TeacherRequest;
//import com.studentmanagement.entity.Teacher;
//import com.studentmanagement.exception.ResourceNotFoundException;
//import com.studentmanagement.repository.TeacherRepository;
//
//@Service
//public class TeacherService {
//
//    private final TeacherRepository repository;
//
//    public TeacherService(TeacherRepository repository) {
//        this.repository = repository;
//    }
//
//    public List<Teacher> getAll() {
//        return repository.findAll();
//    }
//
//    public Teacher getById(Long id) {
//        return repository.findById(id)
//            .orElseThrow(() ->
//                new ResourceNotFoundException("Teacher not found: " + id));
//    }
//
//    @Transactional
//    public Teacher create(TeacherRequest request) {
//        Teacher teacher = new Teacher();
//        apply(teacher, request);
//        return repository.save(teacher);
//    }
//
//    @Transactional
//    public Teacher update(Long id, TeacherRequest request) {
//        Teacher teacher = getById(id);
//        apply(teacher, request);
//        return repository.save(teacher);
//    }
//
//    @Transactional
//    public void delete(Long id) {
//        repository.delete(getById(id));
//    }
//
//    private void apply(Teacher teacher, TeacherRequest request) {
//        teacher.setEmployeeNumber(request.employeeNumber());
//        teacher.setName(request.name());
//        teacher.setEmail(request.email());
//        teacher.setDepartment(request.department());
//        teacher.setPhone(request.phone());
//    }
//}


package com.studentmanagement.service;

import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentmanagement.dto.TeacherRequest;
import com.studentmanagement.entity.AppUser;
import com.studentmanagement.entity.Teacher;
import com.studentmanagement.enums.Role;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.AppUserRepository;
import com.studentmanagement.repository.TeacherRepository;

@Service
public class TeacherService {

    private final TeacherRepository repository;
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(
            TeacherRepository repository,
            AppUserRepository users,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Teacher> getAll() {
        return repository.findAll();
    }

    public Teacher getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Teacher not found: " + id));
    }

    @Transactional
    public Teacher create(TeacherRequest request) {
        String email = request.email()
            .trim()
            .toLowerCase(Locale.ROOT);

        if (request.initialPassword() == null
                || request.initialPassword().length() < 8) {
            throw new IllegalArgumentException(
                "Initial password must contain at least 8 characters");
        }

        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                "Email already has a login account");
        }

        if (repository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                "Teacher email already exists");
        }

        Teacher teacher = new Teacher();
        apply(teacher, request);
        teacher.setEmail(email);

        Teacher savedTeacher = repository.save(teacher);

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPasswordHash(
            passwordEncoder.encode(request.initialPassword()));
        user.setRole(Role.TEACHER);
        user.setEnabled(true);

        users.save(user);

        return savedTeacher;
    }

    @Transactional
    public Teacher update(Long id, TeacherRequest request) {
        Teacher teacher = getById(id);
        apply(teacher, request);
        return repository.save(teacher);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private void apply(Teacher teacher, TeacherRequest request) {
        teacher.setEmployeeNumber(request.employeeNumber());
        teacher.setName(request.name());
        teacher.setEmail(
            request.email().trim().toLowerCase(Locale.ROOT));
        teacher.setDepartment(request.department());
        teacher.setPhone(request.phone());
    }
}

