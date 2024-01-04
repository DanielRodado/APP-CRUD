package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.Schedule;

public class ScheduleDTO {

    // Properties

    private final String id, dayWeek, startTime, timeEnd;

    private final int coursesInThisSchedule;

    // Constructor method

    public ScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.dayWeek = schedule.getDayWeek();
        this.startTime = schedule.getStartTime();
        this.timeEnd = schedule.getTimeEnd();
        this.coursesInThisSchedule = schedule.getCourseSchedules().size();
    }

    // Accessory methods

    public String getId() {
        return id;
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

    public int getCoursesInThisSchedule() {
        return coursesInThisSchedule;
    }
}
