package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.models.*;

import java.time.LocalTime;
import java.util.Set;

public interface CourseScheduleService {

    Set<CourseSchedule> getCourseScheduleBySchedule(Schedule schedule);

    Set<Course> getCoursesFromCourseSchedule(Set<CourseSchedule> courseSchedules);

    Set<Course> getAllCoursesByScheduleStartTimeBetween(LocalTime startRange, LocalTime endRange);

    Set<Course> getAllCoursesByScheduleEndTimeBetween(LocalTime startRange, LocalTime endRange);

    Set<CourseDTO> getCoursesDTOFromCourseSchedule(Set<CourseSchedule> courseSchedules);

    byte countCourseScheduleBySchedule(Schedule schedule);

    byte countCourseScheduleByCourse(Course course);

    boolean existsCourseScheduleByCourseAndSchedule(Course course, Schedule schedule);

    boolean existsCourseScheduleByCourseAndSchedule_DayWeekAndSchedule_ShiftType(Course course,
                                                                                 DayType dayWeek,
                                                                                 ShiftType shiftType);

    void saveCourseSchedule(CourseSchedule courseSchedule);

    void createNewCourseSchedule(Course course, Schedule schedule);

}
