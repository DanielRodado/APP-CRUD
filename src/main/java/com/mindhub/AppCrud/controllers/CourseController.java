package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.DTO.NewCourseApplicationDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.ScheduleUtil.checkRangeOfHours;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @GetMapping("/courses")
    public Set<CourseDTO> getAllCoursesDTO() {
        return courseService.getAllCoursesDTO();
    }

    @GetMapping("/courses/teachers/unassigned")
    public Set<CourseDTO> getCoursesNotTeacher() {
        return courseService.getCoursesDTOWithNullTeacher();
    }

    @PostMapping("/courses")
    public ResponseEntity<String> createNewCourse(@RequestBody NewCourseApplicationDTO newCourseApp) {
        return courseService.createNewCourse(newCourseApp);
    }

    @PatchMapping("/teachers/current/remove/courses")
    public ResponseEntity<String> removeCourseFromTeacher(Authentication teacherCurrent,
                                                          @RequestParam String courseId) {
        return courseService.removeCourseFromTeacher(teacherCurrent.getName(), courseId);
    }

    @PatchMapping("/students/current/add/courses")
    public ResponseEntity<String> addCourseToStudent(Authentication studentCurrent, @RequestParam String courseId) {
        return courseService.addCourseToStudent(studentCurrent.getName(), courseId);
    }

    @PatchMapping("/students/current/remove/courses")
    public ResponseEntity<String> removeCourseFromStudent(Authentication studentCurrent,
                                                          @RequestParam String courseId) {
        return courseService.removeCourseFromStudent(studentCurrent.getName(), courseId);
    }

    @GetMapping("/courses/schedules/start-time-range")
    public ResponseEntity<Object> getCoursesDTOBetweenStartTime(@RequestParam(name = "startRange") LocalTime scheduleStartTimeStartRange,
                                                                @RequestParam(name = "endRange") LocalTime scheduleStartTimeEndRange) {
        return courseService.getCoursesDTOBetweenStartTime(scheduleStartTimeStartRange, scheduleStartTimeEndRange);
    }


    @GetMapping("/courses/schedules/end-time-range")
    public ResponseEntity<Object> getCoursesDTOBetweenEndTime(@RequestParam(name = "startRange") LocalTime scheduleEndTimeStartRange,
                                                                @RequestParam(name = "endRange") LocalTime scheduleEndTimeEndRange) {


        if (scheduleEndTimeStartRange.equals(scheduleEndTimeEndRange)) {
            return new ResponseEntity<>("The start time cannot be the same as the end time.", HttpStatus.FORBIDDEN);
        }

        if (checkRangeOfHours(scheduleEndTimeStartRange, LocalTime.of(8, 0), 2)) {
            return new ResponseEntity<>("The end time must be within two hours of the default start time (08:00).", HttpStatus.FORBIDDEN);
        }

        if (scheduleEndTimeEndRange.isAfter(LocalTime.of(21, 30))) {
            return new ResponseEntity<>("The end time cannot be later than 21:30 hrs.", HttpStatus.FORBIDDEN);
        }

        if (scheduleEndTimeStartRange.isAfter(scheduleEndTimeEndRange)) {
            return new ResponseEntity<>("The start time (" + scheduleEndTimeStartRange + ") cannot be before end time ("
                    + scheduleEndTimeEndRange + ")", HttpStatus.FORBIDDEN);
        }

        if (scheduleEndTimeEndRange.isBefore(scheduleEndTimeStartRange)) {
            return new ResponseEntity<>("The ent time (" + scheduleEndTimeEndRange + ") cannot be later than " +
                    "start time (" + scheduleEndTimeStartRange + ")", HttpStatus.FORBIDDEN);
        }

        if (checkRangeOfHours(scheduleEndTimeStartRange, scheduleEndTimeEndRange, 2)) {
            return new ResponseEntity<>("There must be a minimum of two hours difference between hour ranges.", HttpStatus.FORBIDDEN);
        }

        Set<Course> courses =
                courseScheduleService.getAllCoursesByScheduleEndTimeBetween(scheduleEndTimeStartRange,
                                                                            scheduleEndTimeEndRange);

        return courses.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(courses.stream().map(CourseDTO::new).collect(Collectors.toSet()),
                HttpStatus.OK);

    }


}
