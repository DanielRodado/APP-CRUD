package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.models.*;
import com.mindhub.AppCrud.repositories.CourseScheduleRepository;
import com.mindhub.AppCrud.services.CourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseScheduleServiceImpl implements CourseScheduleService {

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    @Override
    public Set<CourseSchedule> getCourseScheduleBySchedule(Schedule schedule) {
        return courseScheduleRepository.findBySchedule(schedule);
    }

    @Override
    public Set<Course> getCoursesFromCourseSchedule(Set<CourseSchedule> courseSchedules) {
        return courseSchedules.stream().map(CourseSchedule::getCourse).collect(Collectors.toSet());
    }

    @Override
    public Set<Course> getAllCoursesByScheduleStartTimeBetween(LocalTime startRange, LocalTime endRange) {
        return courseScheduleRepository.findCoursesByScheduleStartTimeBetween(startRange, endRange);
    }

    @Override
    public Set<Course> getAllCoursesByScheduleEndTimeBetween(LocalTime startRange, LocalTime endRange) {
        return courseScheduleRepository.findCoursesByScheduleEndTimeBetween(startRange, endRange);
    }

    @Override
    public Set<CourseDTO> getCoursesDTOFromCourseSchedule(Set<CourseSchedule> courseSchedules) {
        return getCoursesFromCourseSchedule(courseSchedules).stream().map(CourseDTO::new).collect(Collectors.toSet());
    }

    @Override
    public byte countCourseScheduleBySchedule(Schedule schedule) {
        return courseScheduleRepository.countBySchedule(schedule);
    }

    @Override
    public byte countCourseScheduleByCourse(Course course) {
        return courseScheduleRepository.countByCourse(course);
    }

    @Override
    public boolean existsCourseScheduleByCourseAndSchedule(Course course, Schedule schedule) {
        return courseScheduleRepository.existsByCourseAndSchedule(course, schedule);
    }

    @Override
    public boolean existsCourseScheduleByCourseAndSchedule_DayWeekAndSchedule_ShiftType(Course course, DayType dayWeek, ShiftType shiftType) {
        return courseScheduleRepository.existsByCourseAndSchedule_DayWeekAndSchedule_ShiftType(course, dayWeek, shiftType);
    }

    @Override
    public void saveCourseSchedule(CourseSchedule courseSchedule) {
        courseScheduleRepository.save(courseSchedule);
    }

}
