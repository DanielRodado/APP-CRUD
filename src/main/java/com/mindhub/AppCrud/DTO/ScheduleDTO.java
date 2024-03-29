package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.DayWeek;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;

import java.time.LocalTime;

public class ScheduleDTO {

    // Properties

    private final String id;

    private final LocalTime startTime, endTime;

    private final DayWeek dayWeek;

    private final ShiftType shiftType;

    private final int coursesInThisSchedule;

    // Constructor method

    public ScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.dayWeek = schedule.getDayWeek();
        this.shiftType = schedule.getShiftType();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.coursesInThisSchedule = schedule.getCourseSchedules().size();
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public DayWeek getDayWeek() {
        return dayWeek;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getCoursesInThisSchedule() {
        return coursesInThisSchedule;
    }
}
