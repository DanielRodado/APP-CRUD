package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.StudentCourse;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentCourseDTO {

    // Properties

    private final String id, courseId, name, place, nameTeacher;

    private final Set<CourseScheduleDTO> schedules;

    private final LocalDate dateOfEntry;

    // Constructor method

    public StudentCourseDTO(StudentCourse studentCourse) {
        this.id = studentCourse.getId();
        this.courseId = studentCourse.getCourse().getId();
        this.name = studentCourse.getCourse().getName();
        this.place = studentCourse.getCourse().getPlace();
        this.nameTeacher = studentCourse.getCourse().getTeacher() != null ?
                studentCourse.getCourse().getTeacher().getFullName() : "";
        this.schedules =
                studentCourse.getCourse().getCourseSchedules().stream().map(CourseScheduleDTO::new).collect(Collectors.toSet());
        this.dateOfEntry = studentCourse.getDateOfEntry();
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getNameTeacher() {
        return nameTeacher;
    }

    public Set<CourseScheduleDTO> getSchedules() {
        return schedules;
    }

    public LocalDate getDateOfEntry() {
        return dateOfEntry;
    }
}
