package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.DTO.NewCourseApplicationDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.*;
import com.mindhub.AppCrud.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
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
    private CourseService courseService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

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

        Set<Schedule> schedules = newCourseApp.idSchedules() != null ? new HashSet<>() : null;

        if (schedules != null) {

            if (schedules.size() >= 2) {
                return new ResponseEntity<>("Courses can only have two schedules.", HttpStatus.NOT_FOUND);
            }

            for (String scheduleId : newCourseApp.idSchedules()) {

                if (!scheduleRepository.existsById(scheduleId)) {
                    return new ResponseEntity<>("One of the schedules could not be found", HttpStatus.NOT_FOUND);
                }

                Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);

                if (courseScheduleRepository.countBySchedule(schedule) >= 5) {
                    return new ResponseEntity<>("There can only be five simultaneous courses per schedule.", HttpStatus.FORBIDDEN);
                }

                schedules.add(schedule);
            }
        }

        try {
            if (!teacherRepository.existsById(newCourseApp.teacherId())) {
                return new ResponseEntity<>("Teacher not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ignored){}

        Course course = new Course(newCourseApp.name(), newCourseApp.place());
        course.setTeacher(newCourseApp.teacherId() != null
                ? teacherRepository.findById(newCourseApp.teacherId()).orElse(null)
                :null);
        courseService.saveCourse(course);

        if (schedules != null) {
            for (Schedule schedule : schedules) {

                CourseSchedule courseSchedule = new CourseSchedule();
                courseSchedule.setCourse(course);
                courseSchedule.setSchedule(schedule);
                courseScheduleRepository.save(courseSchedule);

            }
        }

        return new ResponseEntity<>("Course created!", HttpStatus.CREATED);

    }

    @PatchMapping("/teachers/current/remove/courses")
    public ResponseEntity<String> removeCourseFromTeacher(Authentication teacherCurrent,
                                                          @RequestParam String courseId) {

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!teacherRepository.existsByEmail(teacherCurrent.getName())) {
            return new ResponseEntity<>("The teacher does not exist", HttpStatus.FORBIDDEN);
        }

        if (!courseService.existsCourseByIdAndTeacher(courseId, teacherRepository.findByEmail(teacherCurrent.getName()))) {
            return new ResponseEntity<>("The course does not belong to the teacher", HttpStatus.FORBIDDEN);
        }

        courseService.removeTeacherFromCourseById(courseId);

        return new ResponseEntity<>("Course removed!", HttpStatus.OK);
    }

    @PostMapping("/students/current/add/courses")
    public ResponseEntity<String> addCourseToStudent(Authentication studentCurrent, @RequestParam String courseId) {

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("Course not found", HttpStatus.FORBIDDEN);
        }

        StudentCourse studentCourse = new StudentCourse(LocalDate.now());
        studentCourse.setStudent(studentRepository.findByEmail(studentCurrent.getName()));
        studentCourse.setCourse(courseService.getCourseById(courseId));
        studentCourseRepository.save(studentCourse);

        return new ResponseEntity<>("Course add!", HttpStatus.CREATED);

    }

    @PatchMapping("/students/current/remove/courses")
    public ResponseEntity<String> removeCourseFromStudent(Authentication studentCurrent,
                                                          @RequestParam String courseId) {

        if (!studentRepository.existsByEmail(studentCurrent.getName())) {
            return new ResponseEntity<>("Student not found.", HttpStatus.FORBIDDEN);
        }

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("Course not found.", HttpStatus.FORBIDDEN);
        }

        Student student = studentRepository.findByEmail(studentCurrent.getName());
        Course course = courseService.getCourseById(courseId);

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
