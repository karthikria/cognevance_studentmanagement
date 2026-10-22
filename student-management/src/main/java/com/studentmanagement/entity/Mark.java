
package com.studentmanagement.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
    name = "marks",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {
            "student_id", "course_id", "assessment_name"
        }
    )
)
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank
    @Column(name = "assessment_name", nullable = false)
    private String assessmentName;

    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double score;

    @DecimalMin("0.1")
    @Column(nullable = false)
    private Double maxScore;

    private LocalDate assessmentDate;

    public Mark() {}

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public String getAssessmentName() { return assessmentName; }
    public Double getScore() { return score; }
    public Double getMaxScore() { return maxScore; }
    public LocalDate getAssessmentDate() { return assessmentDate; }

    public void setId(Long id) { this.id = id; }
    public void setStudent(Student student) { this.student = student; }
    public void setCourse(Course course) { this.course = course; }
    public void setAssessmentName(String name) {
        this.assessmentName = name;
    }
    public void setScore(Double score) { this.score = score; }
    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }
    public void setAssessmentDate(LocalDate date) {
        this.assessmentDate = date;
    }
}
