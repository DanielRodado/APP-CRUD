package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.subClass.Teacher;

import java.util.Set;

public interface CourseService {

    Set<Course> getAllCourses();

    Set<Course> getCoursesWithNullTeacher();

    Set<CourseDTO> getAllCoursesDTO();

    Set<CourseDTO> getCoursesDTOWithNullTeacher();

    Course getCourseById(String courseId);

    boolean existsCourseById(String courseId);

    boolean existsCourseByIdAndNotNullTeacher(String courseId);

    boolean existsCourseByIdAndTeacher(String courseId, Teacher teacher);

    void addCourseToTeacherById(String courseId, Teacher teacher);

    void removeTeacherFromCourseById(String courseId);

    void saveCourse(Course course);

}
