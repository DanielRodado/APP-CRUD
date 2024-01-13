package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.subClass.Student;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseDTO {

    // Properties

    private final String id, name, place, nameTeacher;

    private final Set<CourseScheduleDTO> schedules;

    // Constructor methods

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.place = course.getPlace();
        this.nameTeacher = course.getTeacher() != null ? course.getTeacher().getFullName() : "Not teacher";
        this.schedules = course.getCourseSchedules().stream().map(CourseScheduleDTO::new).collect(Collectors.toSet());
    }

    public String getId() {
        return id;
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
}
