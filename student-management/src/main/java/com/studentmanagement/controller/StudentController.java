//package com.studentmanagement.controller;
//
//import com.studentmanagement.entity.Student;
//import com.studentmanagement.repository.StudentRepository;
//
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/students")
//@CrossOrigin(origins = "http://localhost:5173")
//public class StudentController {
//
//    private final StudentRepository repository;
//
//    public StudentController(StudentRepository repository) {
//        this.repository = repository;
//    }
//
//    @GetMapping
//    public List<Student> getAllStudents() {
//        return repository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Student getStudent(@PathVariable Long id) {
//        return repository.findById(id)
//                .orElseThrow(() ->
//                    new RuntimeException("Student not found: " + id));
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Student createStudent(@Valid @RequestBody Student student) {
//        student.setId(null);
//        return repository.save(student);
//    }
//
//    @PutMapping("/{id}")
//    public Student updateStudent(
//            @PathVariable Long id,
//            @Valid @RequestBody Student updatedStudent) {
//
//        Student student = repository.findById(id)
//                .orElseThrow(() ->
//                    new RuntimeException("Student not found: " + id));
//
//        student.setName(updatedStudent.getName());
//        student.setEmail(updatedStudent.getEmail());
////        student.setCourse(updatedStudent.getCourse());
//
//        return repository.save(student);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteStudent(@PathVariable Long id) {
//        if (!repository.existsById(id)) {
//            throw new RuntimeException("Student not found: " + id);
//        }
//
//        repository.deleteById(id);
//    }
//}
package com.studentmanagement.controller;

import java.util.List;

import com.studentmanagement.dto.StudentRequest;
import com.studentmanagement.entity.Student;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private final StudentRepository repository;
    private final StudentService studentService;

    public StudentController(
            StudentRepository repository,
            StudentService studentService) {
        this.repository = repository;
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Student not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(
            @Valid @RequestBody StudentRequest request) {
        return studentService.createStudent(request);
    }

    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student updatedStudent) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Student not found: " + id));

        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());

        return repository.save(student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Student not found: " + id);
        }

        repository.deleteById(id);
    }
}
