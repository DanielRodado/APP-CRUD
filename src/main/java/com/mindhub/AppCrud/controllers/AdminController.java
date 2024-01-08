package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.CourseRepository;
import com.mindhub.AppCrud.repositories.StudentCourseRepository;
import com.mindhub.AppCrud.repositories.StudentRepository;
import com.mindhub.AppCrud.repositories.TeacherRepository;
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
    private StudentCourseRepository studentCourseRepository;

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
    public ResponseEntity<String> removeTeacherToCourse(@RequestParam String courseId) {

        if (!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("The course does not exist", HttpStatus.FORBIDDEN);
        }

        if (!courseRepository.existsByIdAndTeacherIsNotNull(courseId)) {
            return new ResponseEntity<>("This course does not have a teacher", HttpStatus.FORBIDDEN);
        }

        courseRepository.removeCourseToTeacherById(courseId);

        return new ResponseEntity<>("Teacher removed from the course", HttpStatus.OK);
    }

    @PostMapping("/admin/add/students/courses")
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
    public ResponseEntity<String> removeStudentToCourse(@RequestParam String studentId, @RequestParam String courseId) {

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
