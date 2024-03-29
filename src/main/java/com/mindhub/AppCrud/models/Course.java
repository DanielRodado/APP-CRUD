package com.mindhub.AppCrud.models;

import com.mindhub.AppCrud.models.subClass.Teacher;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name, place;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<StudentCourse> studentCourses = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<CourseSchedule> courseSchedules = new HashSet<>();

    // Constructor methods
    public Course() {
    }

    public Course(String name, String place) {
        this.name = name;
        this.place = place;
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Set<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public Set<CourseSchedule> getCourseSchedules() {
        return courseSchedules;
    }

    public void setCourseSchedules(Set<CourseSchedule> courseSchedules) {
        this.courseSchedules = courseSchedules;
    }

    // Methods

    public void addStudentCourse(StudentCourse studentCourse) {
        this.studentCourses.add(studentCourse);
        studentCourse.setCourse(this);
    }

    public void addCourseSchedule(CourseSchedule courseSchedule) {
        this.courseSchedules.add(courseSchedule);
        courseSchedule.setCourse(this);
    }
}
