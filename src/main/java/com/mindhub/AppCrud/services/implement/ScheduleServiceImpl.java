package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.NewScheduleApplicationDTO;
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

import static com.mindhub.AppCrud.utils.ScheduleUtil.checkRangeHourWithShiftType;
import static com.mindhub.AppCrud.utils.ScheduleUtil.checkRangeOfHours;
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
            validateExistsScheduleById(scheduleId);
            Set<CourseSchedule> courseSchedules = courseScheduleService.getCourseScheduleBySchedule(getScheduleById(scheduleId));
            return courseSchedules.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : createResponseWithCourseDTO(courseSchedules);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void validateExistsScheduleById(String scheduleId) {
        if (!existsScheduleById(scheduleId)) {
            throw validationException("No schedule found.");
        }
    }

    @Override
    public ResponseEntity<Object> createResponseWithCourseDTO(Set<CourseSchedule> courseSchedules) {
        return new ResponseEntity<>(courseScheduleService.getCoursesDTOFromCourseSchedule(courseSchedules),
                                     HttpStatus.OK);
    }

    // Create new Schedule
    @Override
    public ResponseEntity<String> createNewSchedule(NewScheduleApplicationDTO newScheduleApp) {
        try {
            validateScheduleApp(newScheduleApp);
            Schedule schedule = createScheduleFromDTO(newScheduleApp);
            saveSchedule(schedule);
            return new ResponseEntity<>("Schedule created!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void validateScheduleApp(NewScheduleApplicationDTO newScheduleApp) {
        validateDayWeek(newScheduleApp.dayWeek());
        validateShiftType(newScheduleApp.shiftType());
        validateExistsSchedule(newScheduleApp);
        validateStarTimeEqualsEndTime(newScheduleApp.startTime(), newScheduleApp.endTime());
        validateStartTimeIsBeforeTo(newScheduleApp.startTime());
        validateStartTimeIsAfterEndTime(newScheduleApp.startTime(), newScheduleApp.endTime());
        validateEndTimeIsAfterTo(newScheduleApp.endTime());
        validateEndTimeIsBeforeStartTime(newScheduleApp.endTime(), newScheduleApp.startTime());
        validateStartTimeAndEndTimeLeastRange(newScheduleApp.startTime(), newScheduleApp.endTime(), 2);
        validateStartTimeAndEndTimeMatchShiftType(newScheduleApp.startTime(), newScheduleApp.endTime(),
                ShiftType.valueOf(newScheduleApp.shiftType()));
    }

    @Override
    public void validateDayWeek(String dayWeek) {
        try {
            DayWeek.valueOf(dayWeek.toUpperCase());
        } catch (Exception ignored) {
            throw validationException("The day of the week entered is not a valid day of the week.");
        }
    }

    @Override
    public void validateShiftType(String shiftType) {
        try {
            ShiftType.valueOf(shiftType.toUpperCase());
        } catch (Exception ignored) {
            throw validationException("The shift type entered is not a valid type.");
        }
    }

    @Override
    public void validateExistsSchedule(NewScheduleApplicationDTO newScheduleApp) {
        if (existsSchedule(DayWeek.valueOf(newScheduleApp.dayWeek()), ShiftType.valueOf(newScheduleApp.shiftType()),
                newScheduleApp.startTime(), newScheduleApp.endTime())) {
            throw validationException("This schedule already exists. Please create another one.");
        }
    }

    @Override
    public void validateStarTimeEqualsEndTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.equals(endTime)) {
            throw validationException("The start time cannot be the same as the end time.");
        }
    }

    @Override
    public void validateStartTimeIsBeforeTo(LocalTime startTime) {
        if (startTime.isBefore(LocalTime.of(8, 0))) {
            throw validationException("The start time cannot be before 8:00 hrs.");
        }
    }

    @Override
    public void validateStartTimeIsAfterEndTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw validationException("The start time cannot be after the end time.");
        }
    }

    @Override
    public void validateEndTimeIsAfterTo(LocalTime endTime) {
        if (endTime.isAfter(LocalTime.of(21, 30))) {
            throw validationException("The end time cannot be later than 21:30 hrs.");
        }
    }

    @Override
    public void validateEndTimeIsBeforeStartTime(LocalTime endTime, LocalTime startTime) {
        if (endTime.isBefore(startTime)) {
            throw validationException("The end time cannot be earlier than the start time.");
        }
    }

    @Override
    public void validateStartTimeAndEndTimeLeastRange(LocalTime startTime, LocalTime endTime, int range) {
        if (checkRangeOfHours(startTime, endTime, range)) {
            throw validationException("There must be a minimum of two hours between the start and end time.");
        }
    }

    @Override
    public void validateStartTimeAndEndTimeMatchShiftType(LocalTime startTime, LocalTime endTime, ShiftType shiftType) {
        if (!(checkRangeHourWithShiftType(startTime, shiftType) && checkRangeHourWithShiftType(endTime, shiftType))) {
            throw validationException("The start and end time must match the shift type.");
        }
    }

    @Override
    public Schedule createScheduleFromDTO(NewScheduleApplicationDTO newScheduleApp) {
        return new Schedule(
                DayWeek.valueOf(newScheduleApp.dayWeek()),
                ShiftType.valueOf(newScheduleApp.shiftType()),
                newScheduleApp.startTime(),
                newScheduleApp.endTime()
        );
    }
}
