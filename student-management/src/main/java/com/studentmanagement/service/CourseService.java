
package com.studentmanagement.service;

import com.studentmanagement.dto.CourseRequest;
import com.studentmanagement.entity.Course;
import com.studentmanagement.entity.Teacher;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.CourseRepository;
import com.studentmanagement.repository.TeacherRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService(
            CourseRepository courseRepository,
            TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Course not found with ID: " + id));
    }

    public Course createCourse(CourseRequest request) {
        Course course = new Course();
        applyRequest(course, request);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseRequest request) {
        Course course = getCourseById(id);
        applyRequest(course, request);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    private void applyRequest(Course course, CourseRequest request) {
        course.setCourseCode(request.courseCode());
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCredits(request.credits());

        if (request.teacherId() != null) {
            Teacher teacher = teacherRepository.findById(
                    request.teacherId()
                )
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Teacher not found with ID: "
                        + request.teacherId()));

            course.setTeacher(teacher);
        } else {
            course.setTeacher(null);
        }
    }
}
