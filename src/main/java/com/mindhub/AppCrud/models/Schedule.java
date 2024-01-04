package com.mindhub.AppCrud.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Schedule {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String dayWeek, startTime, timeEnd;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER)
    private Set<CourseSchedule> courseSchedules = new HashSet<>();

    // Constructor methods

    public Schedule() {
    }

    public Schedule(String dayWeek, String startTime, String timeEnd) {
        this.dayWeek = dayWeek;
        this.startTime = startTime;
        this.timeEnd = timeEnd;
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(String dayWeek) {
        this.dayWeek = dayWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getMaximumCourses() {
        return 5;
    }

    public Set<CourseSchedule> getCourseSchedules() {
        return courseSchedules;
    }

    public void setCourseSchedules(Set<CourseSchedule> courseSchedules) {
        this.courseSchedules = courseSchedules;
    }

    // Methods

    public void addCourseSchedule(CourseSchedule courseSchedule) {
        this.courseSchedules.add(courseSchedule);
        courseSchedule.setSchedule(this);
    }
}
