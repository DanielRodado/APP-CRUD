package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.repositories.CourseRepository;
import com.mindhub.AppCrud.repositories.CourseScheduleRepository;
import com.mindhub.AppCrud.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.CourseUtil.checkRangeOfHours;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    @GetMapping("/courses")
    public Set<CourseDTO> getAllCoursesDTO() {
        return courseRepository.findAll().stream().map(CourseDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/courses/teachers/unassigned")
    public Set<CourseDTO> getCoursesNotTeacher() {
        return courseRepository.findByTeacherIsNull().stream().map(CourseDTO::new).collect(Collectors.toSet());
    }

    @PatchMapping("/courses/teachers/add")
    public ResponseEntity<String> addCourseToTeacher(@RequestParam String teacherId, @RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!teacherRepository.existsById(teacherId)) {
            return new ResponseEntity<>("The teacher does not exist", HttpStatus.FORBIDDEN);
        }

        if (courseRepository.existsByIdAndTeacherIsNotNull(courseId)) {
            return new ResponseEntity<>("This course already has a teacher", HttpStatus.FORBIDDEN);
        }

        courseRepository.addCourseToTeacherById(teacherRepository.findById(teacherId).orElse(null), courseId);

        return new ResponseEntity<>("Course added to the teacher", HttpStatus.OK);
    }

    @PatchMapping("/courses/teachers/delete")
    public ResponseEntity<String> deleteTeacherToCourse(@RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsByIdAndTeacherIsNotNull(courseId)) {
            return new ResponseEntity<>("This course does not have a teacher", HttpStatus.FORBIDDEN);
        }

        courseRepository.deleteCourseToTeacherById(courseId);

        return new ResponseEntity<>("Teacher removed from the course", HttpStatus.OK);
    }

    @GetMapping("/courses/schedules/start-time-range")
    public ResponseEntity<Object> getCoursesDTOBetweenStartTime(@RequestParam(name = "startRange") LocalTime scheduleStartTimeStartRange,
                                                                @RequestParam(name = "endRange") LocalTime scheduleStartTimeEndRange) {


        if (scheduleStartTimeStartRange.equals(scheduleStartTimeEndRange)) {
            return new ResponseEntity<>("The start time cannot be the same as the end time.", HttpStatus.FORBIDDEN);
        }

        if (scheduleStartTimeStartRange.isBefore(LocalTime.of(8, 0))) {
            return new ResponseEntity<>("The start time cannot be before 8:00 hrs.", HttpStatus.FORBIDDEN);
        }

        if (scheduleStartTimeEndRange.isAfter(LocalTime.of(21, 30))) {
            return new ResponseEntity<>("The end time cannot be later than 21:30 hrs.", HttpStatus.FORBIDDEN);
        }

        if (scheduleStartTimeStartRange.isAfter(scheduleStartTimeEndRange)) {
            return new ResponseEntity<>("The start time (" + scheduleStartTimeStartRange + ") cannot be before end time ("
                    + scheduleStartTimeEndRange + ")", HttpStatus.FORBIDDEN);
        }

        if (scheduleStartTimeEndRange.isBefore(scheduleStartTimeStartRange)) {
            return new ResponseEntity<>("The ent time (" + scheduleStartTimeEndRange + ") cannot be later than " +
                    "start time (" + scheduleStartTimeStartRange + ")", HttpStatus.FORBIDDEN);
        }

        if (checkRangeOfHours(scheduleStartTimeStartRange, scheduleStartTimeEndRange, 2)) {
            return new ResponseEntity<>("There must be a minimum of two hours difference between hour ranges.", HttpStatus.FORBIDDEN);
        }

        Set<Course> courses =
                courseScheduleRepository.findCoursesByScheduleStartTimeBetween(scheduleStartTimeStartRange,
                        scheduleStartTimeEndRange);

        return courses.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(courses.stream().map(CourseDTO::new).collect(Collectors.toSet()),
                HttpStatus.OK);
    }

}
