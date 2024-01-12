package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.DayWeek;
import com.mindhub.AppCrud.models.ShiftType;

import java.time.LocalTime;

public class CourseScheduleDTO {

    // Properties

    private final String id, scheduleId;

    private final LocalTime startTime, endTime;

    private final DayWeek dayWeek;

    private final ShiftType shiftType;

    // Constructor method

    public CourseScheduleDTO(CourseSchedule courseSchedule) {
        this.id = courseSchedule.getId();
        this.scheduleId = courseSchedule.getSchedule().getId();
        this.dayWeek = courseSchedule.getSchedule().getDayWeek();
        this.shiftType = courseSchedule.getSchedule().getShiftType();
        this.startTime = courseSchedule.getSchedule().getStartTime();
        this.endTime = courseSchedule.getSchedule().getEndTime();
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getScheduleId() {
        return scheduleId;
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
}
