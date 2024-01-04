package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.subClass.Teacher;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherDTO {

    // Properties

    private final String id, firstName, lastName, email;

    private final List<String> specializations;

    private final Set<CourseDTO> courses;

    // Constructor method

    public TeacherDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.firstName = teacher.getFirstName();
        this.lastName = teacher.getLastName();
        this.email = teacher.getEmail();
        this.specializations = teacher.getSpecialization();
        this.courses = teacher.getCourses().stream().map(CourseDTO::new).collect(Collectors.toSet());
    }

    // Accessory methods

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

    public List<String> getSpecializations() {
        return specializations;
    }

    public Set<CourseDTO> getCourses() {
        return courses;
    }
}
