package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.mindhub.AppCrud.utils.PersonUtil.verifyEmailByType;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @Autowired
    private StudentCourseService studentCourseService;

    @PostMapping("/admin")
    public ResponseEntity<String> createNewStudent(@RequestBody NewPersonApplicationDTO newAdminApp) {

        if (newAdminApp.email().isBlank()) {
            return new ResponseEntity<>("The mail cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (!verifyEmailByType(newAdminApp.email(), "admin")) {
            return new ResponseEntity<>("The email must have an '@'; 'admin', after the '@'; '.com', after 'admin';" +
                    " and no characters after the '.com'", HttpStatus.FORBIDDEN);
        }

        if (adminService.existsAdminByEmail(newAdminApp.email().toLowerCase())) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (newAdminApp.firstName().isBlank()) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (newAdminApp.lastName().isBlank()) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        Admin admin = new Admin(newAdminApp.firstName(), newAdminApp.lastName(), newAdminApp.email().toLowerCase(),
                passwordEncoder.encode(newAdminApp.password()));
        adminService.saveAdmin(admin);

        return new ResponseEntity<>("Admin created!", HttpStatus.CREATED);

    }

    @PatchMapping("/admin/add/schedules/courses")
    public ResponseEntity<String> addScheduleToCourse(@RequestParam String scheduleId, @RequestParam String courseId) {

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("No schedule found.", HttpStatus.NOT_FOUND);
        }

        if (!scheduleService.existsScheduleById(scheduleId)) {
            return new ResponseEntity<>("No schedule found.", HttpStatus.NOT_FOUND);
        }

        Course course = courseService.getCourseById(courseId);
        Schedule schedule = scheduleService.getScheduleById(scheduleId);

        if (courseScheduleService.existsCourseScheduleByCourseAndSchedule(course, schedule)) {
            return new ResponseEntity<>("The course already has this schedule.", HttpStatus.FORBIDDEN);
        }

        if (courseScheduleService.existsCourseScheduleByCourseAndSchedule_DayWeekAndSchedule_ShiftType(course,
                                                                                                       schedule.getDayWeek(),
                                                                                                       schedule.getShiftType())) {
            return new ResponseEntity<>("A course cannot have two schedules in the same shift on the same day.", HttpStatus.FORBIDDEN);
        }

        if (courseScheduleService.countCourseScheduleByCourse(course) >= 2) {
            return new ResponseEntity<>("Courses can only have two schedules.", HttpStatus.NOT_FOUND);
        }

        if (courseScheduleService.countCourseScheduleBySchedule(schedule) >= 5) {
            return new ResponseEntity<>("There can only be five simultaneous courses per schedule.", HttpStatus.FORBIDDEN);
        }

        courseScheduleService.createNewCourseSchedule(course, schedule); // Create and save

        return new ResponseEntity<>("Schedule added to course", HttpStatus.OK);

    }

    @PatchMapping("/admin/add/courses/teachers")
    public ResponseEntity<String> addCourseToTeacher(@RequestParam String teacherId, @RequestParam String courseId) {

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!teacherService.existsTeacherById(teacherId)) {
            return new ResponseEntity<>("The teacher does not exist", HttpStatus.FORBIDDEN);
        }

        if (courseService.existsCourseByIdAndNotNullTeacher(courseId)) {
            return new ResponseEntity<>("This course already has a teacher", HttpStatus.FORBIDDEN);
        }

        courseService.addCourseToTeacherById(courseId, teacherService.getTeacherById(teacherId));

        return new ResponseEntity<>("Course added to the teacher", HttpStatus.OK);
    }

    @PatchMapping("/admin/remove/teachers/courses")
    public ResponseEntity<String> removeTeacherFromCourse(@RequestParam String courseId) {

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!courseService.existsCourseByIdAndNotNullTeacher(courseId)) {
            return new ResponseEntity<>("This course does not have a teacher", HttpStatus.FORBIDDEN);
        }

        courseService.removeTeacherFromCourseById(courseId);

        return new ResponseEntity<>("Teacher removed from the course", HttpStatus.OK);
    }

    @PatchMapping("/admin/add/students/courses")
    public ResponseEntity<String> addStudentToCourse(@RequestParam String studentId, @RequestParam String courseId) {

        if (!studentService.existsStudentById(studentId)) {
            return new ResponseEntity<>("Student not found", HttpStatus.FORBIDDEN);
        }

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("Course not found", HttpStatus.FORBIDDEN);
        }

        studentCourseService.createNewStudentCourse(studentService.getStudentById(studentId), courseService.getCourseById(courseId));

        return new ResponseEntity<>("Student added to course!", HttpStatus.CREATED);
    }

    @PatchMapping("/admin/remove/students/courses")
    public ResponseEntity<String> removeStudentFromCourse(@RequestParam String studentId,
                                                         @RequestParam String courseId) {

        if (!studentService.existsStudentById(studentId)) {
            return new ResponseEntity<>("Student not found.", HttpStatus.FORBIDDEN);
        }

        if (!courseService.existsCourseById(courseId)) {
            return new ResponseEntity<>("Course not found.", HttpStatus.FORBIDDEN);
        }

        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        if (!studentCourseService.existsStudentCourseByStudentAndCourse(student, course)) {
            return new ResponseEntity<>("The student is not in the course.", HttpStatus.FORBIDDEN);
        }

        studentCourseService.softDeleteStudentCourse(student, course);

        return new ResponseEntity<>("Course removed to student!", HttpStatus.OK);

    }

}
