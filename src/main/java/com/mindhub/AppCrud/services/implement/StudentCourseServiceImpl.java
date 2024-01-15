package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.StudentCourseRepository;
import com.mindhub.AppCrud.services.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Override
    public boolean existsStudentCourseByStudentAndCourse(Student student, Course course) {
        return studentCourseRepository.existsByStudentAndCourse(student, course);
    }

    @Override
    public byte countStudentCourseByCourseAndIsActive(Course course, Boolean isActive) {
        return studentCourseRepository.countByCourseAndIsActive(course, isActive);
    }

    @Override
    public void softDeleteStudentCourse(Student student, Course course) {
        studentCourseRepository.softDeleteStudentCourse(student, course);
    }

    @Override
    public void saveStudentCourse(StudentCourse studentCourse) {
        studentCourseRepository.save(studentCourse);
    }

    @Override
    public StudentCourse createNewStudentCourse(Student student, Course course) {
        StudentCourse studentCourse = new StudentCourse(LocalDate.now());
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        return studentCourse;
    }

}
