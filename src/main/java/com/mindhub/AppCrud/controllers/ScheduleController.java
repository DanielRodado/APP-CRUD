package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewScheduleApplicationDTO;
import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.services.CourseScheduleService;
import com.mindhub.AppCrud.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
        return scheduleService.getAllCoursesDTOBySchedule(scheduleId);
    }

    @PostMapping("/schedules")
    public ResponseEntity<String> createNewSchedule(@RequestBody NewScheduleApplicationDTO newScheduleApp) {
        return scheduleService.createNewSchedule(newScheduleApp);
    }

}
