package com.mindhub.AppCrud.models;

import com.mindhub.AppCrud.models.subClass.Student;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class StudentCourse {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate dateOfEntry;

    private Boolean isActive = true;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    // Constructor methods

    public StudentCourse() {
    }

    public StudentCourse(LocalDate dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public LocalDate getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(LocalDate dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
