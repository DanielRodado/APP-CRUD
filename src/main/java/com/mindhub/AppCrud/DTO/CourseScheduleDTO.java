package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.DayType;
import com.mindhub.AppCrud.models.ShiftType;

public class CourseScheduleDTO {

    // Properties

    private final String id, scheduleId, startTime, timeEnd;

    private final DayType dayWeek;

    private final ShiftType shiftType;

    // Constructor method

    public CourseScheduleDTO(CourseSchedule courseSchedule) {
        this.id = courseSchedule.getId();
        this.scheduleId = courseSchedule.getSchedule().getId();
        this.dayWeek = courseSchedule.getSchedule().getDayWeek();
        this.shiftType = courseSchedule.getSchedule().getShiftType();
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

    public DayType getDayWeek() {
        return dayWeek;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTimeEnd() {
        return timeEnd;
    }
}
