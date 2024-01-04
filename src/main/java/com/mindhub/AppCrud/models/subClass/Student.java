package com.mindhub.AppCrud.models.subClass;

import com.mindhub.AppCrud.models.Person;
import com.mindhub.AppCrud.models.StudentCourse;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Student extends Person {

    // Properties

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<StudentCourse> studentCourses = new HashSet<>();

    // Constructor methods

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    // Accessory methods

    public Set<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Set<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    // Methods

    public void addStudentCourse(StudentCourse studentCourse) {
        this.studentCourses.add(studentCourse);
        studentCourse.setStudent(this);
    }

}
