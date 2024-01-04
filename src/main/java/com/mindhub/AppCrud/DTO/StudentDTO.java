package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.Person;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.models.subClass.Teacher;

import java.util.Set;
import java.util.stream.Collectors;

public class StudentDTO {

    private final String id, firstName, lastName, email;

    private final Set<StudentCourseDTO> courses;

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.courses = student.getStudentCourses().stream().map(StudentCourseDTO::new).collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<StudentCourseDTO> getCourses() {
        return courses;
    }
}
