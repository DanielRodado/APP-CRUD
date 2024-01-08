package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @PatchMapping("/admin/add/schedules/courses")
    public ResponseEntity<String> addScheduleToCourse(@RequestParam String scheduleId, @RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("No schedule found.", HttpStatus.NOT_FOUND);
        }

        if (!scheduleRepository.existsById(scheduleId)) {
            return new ResponseEntity<>("No schedule found.", HttpStatus.NOT_FOUND);
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);

        if (courseScheduleRepository.existsByCourseAndSchedule(course, schedule)) {
            return new ResponseEntity<>("The course already has this schedule.", HttpStatus.FORBIDDEN);
        }

        if (courseScheduleRepository.existsByCourseAndSchedule_DayWeekAndSchedule_ShiftType(
                course, schedule.getDayWeek(), schedule.getShiftType())) {
            return new ResponseEntity<>("A course cannot have two schedules in the same shift on the same day.", HttpStatus.FORBIDDEN);
        }

        if (courseScheduleRepository.countByCourse(course) >= 2) {
            return new ResponseEntity<>("Courses can only have two schedules.", HttpStatus.NOT_FOUND);
        }

        if (courseScheduleRepository.countBySchedule(schedule) >= 5) {
            return new ResponseEntity<>("There can only be five simultaneous courses per schedule.", HttpStatus.FORBIDDEN);
        }

        CourseSchedule courseSchedule = new CourseSchedule();
        courseSchedule.setSchedule(schedule);
        courseSchedule.setCourse(course);
        courseScheduleRepository.save(courseSchedule);

        return new ResponseEntity<>("Schedule added to course", HttpStatus.OK);

    }

    @PatchMapping("/admin/add/courses/teachers")
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

    @PatchMapping("/admin/remove/teachers/courses")
    public ResponseEntity<String> removeTeacherFromCourse(@RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsByIdAndTeacherIsNotNull(courseId)) {
            return new ResponseEntity<>("This course does not have a teacher", HttpStatus.FORBIDDEN);
        }

        courseRepository.removeCourseToTeacherById(courseId);

        return new ResponseEntity<>("Teacher removed from the course", HttpStatus.OK);
    }

    @PatchMapping("/admin/add/students/courses")
    public ResponseEntity<String> addStudentToCourse(@RequestParam String studentId, @RequestParam String courseId) {

        if (!studentRepository.existsById(studentId)) {
            return new ResponseEntity<>("Student not found", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("Course not found", HttpStatus.FORBIDDEN);
        }

        StudentCourse studentCourse = new StudentCourse(LocalDate.now());
        studentCourse.setStudent(studentRepository.findById(studentId).orElse(null));
        studentCourse.setCourse(courseRepository.findById(courseId).orElse(null));
        studentCourseRepository.save(studentCourse);

        return new ResponseEntity<>("Student added to course!", HttpStatus.CREATED);
    }

    @PatchMapping("/admin/remove/students/courses")
    public ResponseEntity<String> removeStudentFromCourse(@RequestParam String studentId,
                                                         @RequestParam String courseId) {

        if (!studentRepository.existsById(studentId)) {
            return new ResponseEntity<>("Student not found.", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("Course not found.", HttpStatus.FORBIDDEN);
        }

        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (!studentCourseRepository.existsByStudentAndCourse(student, course)) {
            return new ResponseEntity<>("The student is not in the course.", HttpStatus.FORBIDDEN);
        }

        studentCourseRepository.softDeleteStudentCourse(student, course);

        return new ResponseEntity<>("Course removed to student!", HttpStatus.OK);

    }

}