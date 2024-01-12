package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.DayType;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Set;

public interface ScheduleService {

    // Methods repository

    Set<Schedule> getAllSchedules();

    Set<ScheduleDTO> getAllSchedulesDTO();

    Schedule getScheduleById(String scheduleId);

    boolean existsScheduleById(String scheduleId);

    boolean existsSchedule(DayType dayWeek, ShiftType shiftType, LocalTime startTime, LocalTime timeEnd);

    void saveSchedule(Schedule schedule);

    // Methods controllers

    // Get All Courses By Schedule
    ResponseEntity<Object> getAllCoursesDTOBySchedule(String scheduleId);

    void validateExistsSchedule(String scheduleId);

    ResponseEntity<Object> createResponseWithCourseDTO(Set<CourseSchedule> courseSchedules);

}
