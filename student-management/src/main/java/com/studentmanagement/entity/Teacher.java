
package com.studentmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank private String employeeNumber;

    @Column(nullable = false)
    @NotBlank private String name;

    @Column(nullable = false, unique = true)
    @Email @NotBlank private String email;

    private String department;
    private String phone;

    public Teacher() {}

    public Long getId() { return id; }
    public String getEmployeeNumber() { return employeeNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getPhone() { return phone; }

    public void setId(Long id) { this.id = id; }
    public void setEmployeeNumber(String v) { employeeNumber = v; }
    public void setName(String v) { name = v; }
    public void setEmail(String v) { email = v; }
    public void setDepartment(String v) { department = v; }
    public void setPhone(String v) { phone = v; }
}
