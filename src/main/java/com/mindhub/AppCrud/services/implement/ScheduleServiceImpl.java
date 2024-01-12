package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.DayWeek;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;
import com.mindhub.AppCrud.repositories.ScheduleRepository;
import com.mindhub.AppCrud.services.CourseScheduleService;
import com.mindhub.AppCrud.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.ValidationExceptionUtil.validationException;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CourseScheduleService courseScheduleService;

    // Methods repository

    @Override
    public Set<Schedule> getAllSchedules() {
        return new HashSet<>(scheduleRepository.findAll());
    }

    @Override
    public Set<ScheduleDTO> getAllSchedulesDTO() {
        return getAllSchedules().stream().map(ScheduleDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Schedule getScheduleById(String scheduleId) {
        return scheduleRepository.findById(scheduleId).orElse(null);
    }

    @Override
    public boolean existsScheduleById(String scheduleId) {
        return scheduleRepository.existsById(scheduleId);
    }

    @Override
    public boolean existsSchedule(DayWeek dayWeek, ShiftType shiftType, LocalTime startTime, LocalTime endTime) {
        return scheduleRepository.existsByDayWeekAndShiftTypeAndStartTimeAndEndTime(dayWeek, shiftType, startTime, endTime);
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    // Methods controllers

    // Get All Courses By Schedule
    @Override
    public ResponseEntity<Object> getAllCoursesDTOBySchedule(String scheduleId) {
        try {
            validateExistsSchedule(scheduleId);
            Set<CourseSchedule> courseSchedules = courseScheduleService.getCourseScheduleBySchedule(getScheduleById(scheduleId));
            return courseSchedules.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : createResponseWithCourseDTO(courseSchedules);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void validateExistsSchedule(String scheduleId) {
        if (!existsScheduleById(scheduleId)) {
            throw validationException("No schedule found.");
        }
    }

    @Override
    public ResponseEntity<Object> createResponseWithCourseDTO(Set<CourseSchedule> courseSchedules) {
        return new ResponseEntity<>(courseScheduleService.getCoursesDTOFromCourseSchedule(courseSchedules),
                                     HttpStatus.OK);
    }
}
