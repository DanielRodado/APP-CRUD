package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.CourseRepository;
import com.mindhub.AppCrud.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Set<Course> getAllCourses() {
        return new HashSet<>(courseRepository.findAll());
    }

    @Override
    public Set<Course> getCoursesWithNullTeacher() {
        return courseRepository.findByTeacherIsNull();
    }

    @Override
    public Set<CourseDTO> getAllCoursesDTO() {
        return getAllCourses().stream().map(CourseDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<CourseDTO> getCoursesDTOWithNullTeacher() {
        return getCoursesWithNullTeacher().stream().map(CourseDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Course getCourseById(String courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    @Override
    public boolean existsCourseById(String courseId) {
        return courseRepository.existsById(courseId);
    }

    @Override
    public boolean existsCourseByIdAndNotNullTeacher(String courseId) {
        return courseRepository.existsByIdAndTeacherIsNotNull(courseId);
    }

    @Override
    public boolean existsCourseByIdAndTeacher(String courseId, Teacher teacher) {
        return courseRepository.existsByIdAndTeacher(courseId, teacher);
    }

    @Override
    public void addCourseToTeacherById(String courseId, Teacher teacher) {
        courseRepository.addCourseToTeacherById(courseId, teacher);
    }

    @Override
    public void removeTeacherFromCourseById(String courseId) {
        courseRepository.removeTeacherFromCourseById(courseId);
    }

    @Override
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

}
