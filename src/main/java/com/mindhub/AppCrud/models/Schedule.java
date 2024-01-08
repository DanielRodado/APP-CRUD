package com.mindhub.AppCrud.models;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Schedule {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private DayType dayWeek;

    private ShiftType shiftType;

    private LocalTime startTime, endTime;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER)
    private Set<CourseSchedule> courseSchedules = new HashSet<>();

    // Constructor methods

    public Schedule() {
    }

    public Schedule(DayType dayWeek, ShiftType shiftType, LocalTime startTime, LocalTime endTime) {
        this.dayWeek = dayWeek;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public DayType getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(DayType dayWeek) {
        this.dayWeek = dayWeek;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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

    public Set<Course> getCourses() {
        return this.courseSchedules.stream().map(CourseSchedule::getCourse).collect(Collectors.toSet());
    }

}
