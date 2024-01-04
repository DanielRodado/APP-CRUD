package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.CourseSchedule;

public class CourseScheduleDTO {

    // Properties

    private final String id, scheduleId, dayWeek, startTime, timeEnd;

    // Constructor method

    public CourseScheduleDTO(CourseSchedule courseSchedule) {
        this.id = courseSchedule.getId();
        this.scheduleId = courseSchedule.getSchedule().getId();
        this.dayWeek = courseSchedule.getSchedule().getDayWeek();
        this.startTime = courseSchedule.getSchedule().getStartTime();
        this.timeEnd = courseSchedule.getSchedule().getTimeEnd();
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getDayWeek() {
        return dayWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTimeEnd() {
        return timeEnd;
    }
}
