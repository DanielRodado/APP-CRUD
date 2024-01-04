package com.mindhub.AppCrud.models;

import jakarta.persistence.*;

@Entity
public class CourseSchedule {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Schedule schedule;

    // Constructor method

    public CourseSchedule() {
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
