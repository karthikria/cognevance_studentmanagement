
package com.studentmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank private String courseCode;

    @Column(nullable = false)
    @NotBlank private String title;

    private String description;

    @Min(1) @Max(10)
    private Integer credits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Course() {}

    public Long getId() { return id; }
    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getCredits() { return credits; }
    public Teacher getTeacher() { return teacher; }

    public void setId(Long id) { this.id = id; }
    public void setCourseCode(String v) { courseCode = v; }
    public void setTitle(String v) { title = v; }
    public void setDescription(String v) { description = v; }
    public void setCredits(Integer v) { credits = v; }
    public void setTeacher(Teacher v) { teacher = v; }
}
