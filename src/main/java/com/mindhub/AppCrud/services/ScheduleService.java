package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.DayType;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;

import java.time.LocalTime;
import java.util.Set;

public interface ScheduleService {

    Set<Schedule> getAllSchedules();

    Set<ScheduleDTO> getAllSchedulesDTO();

    Schedule getScheduleById(String scheduleId);

    boolean existsScheduleById(String scheduleId);

    boolean existsSchedule(DayType dayWeek, ShiftType shiftType, LocalTime startTime, LocalTime timeEnd);

    void saveSchedule(Schedule schedule);

}
