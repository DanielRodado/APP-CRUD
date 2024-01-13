package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.DTO.NewCourseApplicationDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.subClass.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface CourseService {

    // Methods repository

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

    // Methods controllers

    HttpStatus getHttpStatusByCondition(String text);

    // Create new Course
    ResponseEntity<String> createNewCourse(NewCourseApplicationDTO newCourseApp);

    void validateCourseApp(NewCourseApplicationDTO newCourseApp);

    void validateName(String name);

    void validatePlace(String place);

    void validateExistsTeacherById(String teacherId);

    void validateSchedules(Set<String> idSchedules);

    void validateLengthSetIdSchedules(Set<String> idSchedules);

    void validationsSchedulesByFor(Set<String> idSchedules);

    void validateExistsSchedule(String scheduleId);

    void validateQuantityCoursesInSchedule(String scheduleId);

    Course createCourseFromDTO(NewCourseApplicationDTO newCourseApp);

    Set<Schedule> findSchedules(Set<String> idSchedules);

    void addSchedulesToSet(Set<Schedule> schedules, Set<String> idSchedules);

    void assignRelationships(Course course, String teacherId, Set<String> idSchedules);

    void assignTeacherToCourse(Course course, String teacherId);

    void assignSchedulesToCourse(Course course, Set<Schedule> schedules);

    // Remove course from Teacher
    ResponseEntity<String> removeCourseFromTeacher(String teacherEmail, String courseId);

    void validationsBeforeRemoveCourseFromTeacher(String teacherEmail, String courseId);

    void validateExistsCourseById(String courseId);

    void validateCourseInTeacher(String courseId, String teacherEmail);

}
