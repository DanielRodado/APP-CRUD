package com.mindhub.AppCrud.models.subClass;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.Person;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Teacher extends Person {

    // Properties

    @ElementCollection
    private List<String> specializations;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();

   // Constructor methods

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String email, String password, List<String> specialization) {
        super(firstName, lastName, email, password);
        this.specializations = specialization;
    }

    // Accessory methods

    public List<String> getSpecialization() {
        return specializations;
    }

    public void setSpecialization(List<String> specializations) {
        this.specializations = specializations;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // Methods

    public void addCourse(Course course) {
        this.courses.add(course);
        course.setTeacher(this);
    }
}
