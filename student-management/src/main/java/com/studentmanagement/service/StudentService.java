
package com.studentmanagement.service;

import java.util.List;
import java.util.Locale;

import com.studentmanagement.dto.StudentRequest;
import com.studentmanagement.entity.AppUser;
import com.studentmanagement.enums.Role;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.AppUserRepository;
import com.studentmanagement.repository.StudentRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public StudentService(
            StudentRepository repository,
            AppUserRepository users,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Student not found with ID: " + id
                )
            );
    }

    @Transactional
    public Student createStudent(StudentRequest request) {
        String email = request.email()
            .trim()
            .toLowerCase(Locale.ROOT);

        if (repository.findByEmailIgnoreCase(email).isPresent()
                || users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                "An account or student with this email already exists."
            );
        }

        if (request.initialPassword() == null
                || request.initialPassword().isBlank()
                || request.initialPassword().length() < 8
                || request.initialPassword().length() > 72) {
            throw new IllegalArgumentException(
                "Initial password must be between 8 and 72 characters."
            );
        }

        Student student = new Student();
        applyRequest(student, request);
        student.setEmail(email);

        Student savedStudent = repository.save(student);

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPasswordHash(
            passwordEncoder.encode(request.initialPassword())
        );
        user.setRole(Role.STUDENT);
        user.setEnabled(true);

        users.save(user);

        return savedStudent;
    }

    @Transactional
    public Student updateStudent(Long id, StudentRequest request) {

        Student student = getStudentById(id);
        String oldEmail = student.getEmail();

        String email = request.email()
            .trim()
            .toLowerCase(Locale.ROOT);

        boolean emailUsedByAnotherStudent =
            repository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(id))
                .isPresent();

        boolean emailUsedByAnotherAccount =
            users.findByEmailIgnoreCase(email)
                .filter(account ->
                    !account.getEmail().equalsIgnoreCase(oldEmail))
                .isPresent();

        if (emailUsedByAnotherStudent || emailUsedByAnotherAccount) {
            throw new IllegalArgumentException(
                "This email is already used by another account."
            );
        }

        student.setStudentNumber(request.studentNumber());
        student.setName(request.name());
        student.setEmail(email);
        student.setPhone(request.phone());
        student.setDepartment(request.department());
        student.setYearOfStudy(request.yearOfStudy());

        // Keep the student's login email synchronized.
        if (!oldEmail.equalsIgnoreCase(email)) {
            users.findByEmailIgnoreCase(oldEmail)
                .ifPresent(account -> {
                    account.setEmail(email);
                    users.save(account);
                });
        }

        return repository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);

        // Disable the login account before deleting the student.
        users.findByEmailIgnoreCase(student.getEmail())
            .ifPresent(account -> {
                account.setEnabled(false);
                users.save(account);
            });

        repository.delete(student);
    }

    private void applyRequest(
            Student student, StudentRequest request) {
        student.setStudentNumber(request.studentNumber());
        student.setName(request.name());
        student.setEmail(
            request.email().trim().toLowerCase(Locale.ROOT)
        );
        student.setPhone(request.phone());
        student.setDepartment(request.department());
        student.setYearOfStudy(request.yearOfStudy());
    }
}
