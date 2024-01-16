package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.NewScheduleApplicationDTO;
import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Set;

public interface ScheduleService {

    // Methods repository

    Set<Schedule> getAllSchedules();

    Set<ScheduleDTO> getAllSchedulesDTO();

    Schedule getScheduleById(String scheduleId);

    boolean existsScheduleById(String scheduleId);

    boolean existsSchedule(DayWeek dayWeek, ShiftType shiftType, LocalTime startTime, LocalTime timeEnd);

    void saveSchedule(Schedule schedule);

    // Methods controllers

    // Get All Courses By Schedule
    ResponseEntity<Object> getAllCoursesDTOBySchedule(String scheduleId);

    void validateExistsScheduleById(String scheduleId);

    ResponseEntity<Object> createResponseWithCourseDTO(Set<CourseSchedule> courseSchedules);

    // Create new Schedule
    ResponseEntity<String> createNewSchedule(NewScheduleApplicationDTO newScheduleApp);

    void validateScheduleApp(NewScheduleApplicationDTO newScheduleApp);

    void validateDayWeek(String dayWeek);

    void validateShiftType(String shiftType);

    void validateExistsSchedule(NewScheduleApplicationDTO newScheduleApp);

    void validateStarTimeEqualsEndTime(LocalTime startTime, LocalTime endTime, String typeCondition);

    void validateStartTimeIsBeforeTo(LocalTime startTime, String typeCondition);

    void validateStartTimeIsAfterEndTime(LocalTime startTime, LocalTime endTime, String typeCondition);

    void validateEndTimeIsAfterTo(LocalTime endTime, String typeCondition);

    void validateEndTimeIsBeforeStartTime(LocalTime endTime, LocalTime startTime, String typeCondition);

    void validateStartTimeAndEndTimeLeastRange(LocalTime startTime, LocalTime endTime, int range);

    void validateStartTimeAndEndTimeMatchShiftType(LocalTime startTime, LocalTime endTime, ShiftType shiftType);

    Schedule createScheduleFromDTO(NewScheduleApplicationDTO newScheduleApp);

}
