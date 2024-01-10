package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewScheduleApplicationDTO;
import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.*;
import com.mindhub.AppCrud.services.CourseScheduleService;
import com.mindhub.AppCrud.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Set;

import static com.mindhub.AppCrud.utils.ScheduleUtil.checkRangeHourWithTypeDay;
import static com.mindhub.AppCrud.utils.ScheduleUtil.checkRangeOfHours;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @GetMapping("/schedules")
    public Set<ScheduleDTO> getAllSchedulesDTO() {
        return scheduleService.getAllSchedulesDTO();
    }

    @GetMapping("/schedules/courses")
    public ResponseEntity<Object> getAllCoursesDTOBySchedule(@RequestParam String scheduleId) {

        if (!scheduleService.existsScheduleById(scheduleId)) {
            return new ResponseEntity<>("No schedule found.", HttpStatus.NOT_FOUND);
        }

        Set<CourseSchedule> courseSchedules =
                courseScheduleService.getCourseScheduleBySchedule(scheduleService.getScheduleById(scheduleId));

        return courseSchedules.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(courseScheduleService.getCoursesDTOFromCourseSchedule(courseSchedules), HttpStatus.OK);
    }

    @PostMapping("/schedules")
    public ResponseEntity<String> createNewSchedules(@RequestBody NewScheduleApplicationDTO newScheduleApp) {

        try {
            DayType.valueOf(newScheduleApp.dayWeek().toUpperCase());
        } catch (Exception ignored) {
            return new ResponseEntity<>("The day of the week entered is not a valid day of the week.",
                    HttpStatus.FORBIDDEN);
        }

        try {
            ShiftType.valueOf(newScheduleApp.shiftType().toUpperCase());
        } catch (Exception ignored) {
            return new ResponseEntity<>("The shift type entered is not a valid type.", HttpStatus.FORBIDDEN);
        }

        if (scheduleService.existsSchedule(DayType.valueOf(newScheduleApp.dayWeek()),
                ShiftType.valueOf(newScheduleApp.shiftType()), newScheduleApp.startTime(), newScheduleApp.endTime())) {
            return new ResponseEntity<>("This schedule already exists. Please create another one.",
                    HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.startTime().equals(newScheduleApp.endTime())) {
            return new ResponseEntity<>("The start time cannot be the same as the end time.", HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.startTime().isBefore(LocalTime.of(8, 0))) {
            return new ResponseEntity<>("The start time cannot be before 8:00 hrs.", HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.startTime().isAfter(newScheduleApp.endTime())) {
            return new ResponseEntity<>("The start time cannot be after the end time.", HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.endTime().isAfter(LocalTime.of(21, 30))) {
            return new ResponseEntity<>("The end time cannot be later than 21:30 hrs.", HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.endTime().isBefore(newScheduleApp.startTime())) {
            return new ResponseEntity<>("The end time cannot be earlier than the start time.", HttpStatus.FORBIDDEN);
        }

        if (checkRangeOfHours(newScheduleApp.startTime(), newScheduleApp.endTime(), 2)) {
            return new ResponseEntity<>("There must be a minimum of two hours between the start and end time.", HttpStatus.FORBIDDEN);
        }

        if (!(checkRangeHourWithTypeDay(newScheduleApp.startTime(), ShiftType.valueOf(newScheduleApp.shiftType()))
                && checkRangeHourWithTypeDay(newScheduleApp.endTime(), ShiftType.valueOf(newScheduleApp.shiftType())))) {
            return new ResponseEntity<>("The start and end time must match the shift type.", HttpStatus.FORBIDDEN);
        }

        Schedule schedule = new Schedule(DayType.valueOf(newScheduleApp.dayWeek()), ShiftType.valueOf(newScheduleApp.shiftType()),
                newScheduleApp.startTime(), newScheduleApp.endTime());

        scheduleService.saveSchedule(schedule);

        return new ResponseEntity<>("Schedule created!", HttpStatus.CREATED);
    }

}
