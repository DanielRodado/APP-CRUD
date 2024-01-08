package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.ScheduleUtil.checkRangeOfHours;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

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

    @PatchMapping("/teachers/current/remove/courses")
    public ResponseEntity<String> removeCourseFromTeacher(Authentication teacherCurrent,
                                                          @RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!teacherRepository.existsByEmail(teacherCurrent.getName())) {
            return new ResponseEntity<>("The teacher does not exist", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsByIdAndTeacher(courseId, teacherRepository.findByEmail(teacherCurrent.getName()))) {
            return new ResponseEntity<>("The course does not belong to the teacher", HttpStatus.FORBIDDEN);
        }

        courseRepository.removeCourseToTeacherById(courseId);

        return new ResponseEntity<>("Course removed!", HttpStatus.OK);
    }

    @PostMapping("/students/current/add/courses")
    public ResponseEntity<String> addCourseToStudent(Authentication studentCurrent, @RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("Course not found", HttpStatus.FORBIDDEN);
        }

        StudentCourse studentCourse = new StudentCourse(LocalDate.now());
        studentCourse.setStudent(studentRepository.findByEmail(studentCurrent.getName()));
        studentCourse.setCourse(courseRepository.findById(courseId).orElse(null));
        studentCourseRepository.save(studentCourse);

        return new ResponseEntity<>("Course add!", HttpStatus.CREATED);

    }

    @PatchMapping("/students/current/remove/courses")
    public ResponseEntity<String> removeCourseFromStudent(Authentication studentCurrent,
                                                          @RequestParam String courseId) {

        if (!studentRepository.existsByEmail(studentCurrent.getName())) {
            return new ResponseEntity<>("Student not found.", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("Course not found.", HttpStatus.FORBIDDEN);
        }

        Student student = studentRepository.findByEmail(studentCurrent.getName());
        Course course = courseRepository.findById(courseId).orElse(null);

        if (!studentCourseRepository.existsByStudentAndCourse(student, course)) {
            return new ResponseEntity<>("The student is not in the course.", HttpStatus.FORBIDDEN);
        }

        studentCourseRepository.softDeleteStudentCourse(student, course);

        return new ResponseEntity<>("Course removed!", HttpStatus.OK);

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

        if (scheduleStartTimeStartRange.isAfter(scheduleStartTimeEndRange)) {
            return new ResponseEntity<>("The start time (" + scheduleStartTimeStartRange + ") cannot be before end time ("
                    + scheduleStartTimeEndRange + ")", HttpStatus.FORBIDDEN);
        }

        if (scheduleStartTimeEndRange.isBefore(scheduleStartTimeStartRange)) {
            return new ResponseEntity<>("The ent time (" + scheduleStartTimeEndRange + ") cannot be later than " +
                    "start time (" + scheduleStartTimeStartRange + ")", HttpStatus.FORBIDDEN);
        }

        if (checkRangeOfHours(scheduleStartTimeEndRange, LocalTime.of(21, 30), 2)) {
            return new ResponseEntity<>("The start time must be within two hours of the default end time (21:30).", HttpStatus.FORBIDDEN);
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
                courseScheduleRepository.findCoursesByScheduleEndTimeBetween(scheduleEndTimeStartRange,
                        scheduleEndTimeEndRange);

        return courses.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(courses.stream().map(CourseDTO::new).collect(Collectors.toSet()),
                HttpStatus.OK);
    }


}
