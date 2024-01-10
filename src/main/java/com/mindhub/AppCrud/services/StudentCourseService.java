package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;

public interface StudentCourseService {

    boolean existsStudentCourseByStudentAndCourse(Student student, Course course);

    void softDeleteStudentCourse(Student student, Course course);

    void saveStudentCourse(StudentCourse studentCourse);

    void createNewStudentCourse(Student student, Course course);

}
