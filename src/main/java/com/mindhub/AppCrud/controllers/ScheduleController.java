package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewScheduleApplicationDTO;
import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.DayType;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;
import com.mindhub.AppCrud.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/schedules")
    public Set<ScheduleDTO> getAllSchedulesDTO() {
        return scheduleRepository.findAll().stream().map(ScheduleDTO::new).collect(Collectors.toSet());
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

        if (scheduleRepository.existsByDayWeekAndShiftTypeAndStartTimeAndEndTime(DayType.valueOf(newScheduleApp.dayWeek()),
                ShiftType.valueOf(newScheduleApp.shiftType()), newScheduleApp.startTime(), newScheduleApp.timeEnd())) {
            return new ResponseEntity<>("This schedule already exists. Please create another one.",
                    HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.startTime().equals(newScheduleApp.timeEnd())) {
            return new ResponseEntity<>("The start time cannot be the same as the end time.", HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.startTime().isBefore(LocalTime.of(8, 0))) {
            return new ResponseEntity<>("The start time cannot be before 8:00 hrs.", HttpStatus.FORBIDDEN);
        }

        if (newScheduleApp.timeEnd().isAfter(LocalTime.of(21, 30))) {
            return new ResponseEntity<>("The end time cannot be later than 21:30 hrs.", HttpStatus.FORBIDDEN);
        }

        Schedule schedule = new Schedule(DayType.valueOf(newScheduleApp.dayWeek()), ShiftType.valueOf(newScheduleApp.shiftType()),
                newScheduleApp.startTime(), newScheduleApp.timeEnd());

        scheduleRepository.save(schedule);

        return new ResponseEntity<>("Schedule created!", HttpStatus.CREATED);
    }

}
