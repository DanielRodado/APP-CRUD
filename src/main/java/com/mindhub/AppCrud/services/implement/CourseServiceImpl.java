package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.DTO.NewCourseApplicationDTO;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.CourseRepository;
import com.mindhub.AppCrud.services.CourseScheduleService;
import com.mindhub.AppCrud.services.CourseService;
import com.mindhub.AppCrud.services.ScheduleService;
import com.mindhub.AppCrud.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.ValidationExceptionUtil.validationException;

@Service
public class CourseServiceImpl implements CourseService {

    // Methods repository

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseScheduleService courseScheduleService;

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


    // Methods controllers

    // Create new Course
    @Override
    public ResponseEntity<String> createNewCourse(NewCourseApplicationDTO newCourseApp) {
        try {
            validateCourseApp(newCourseApp);
            Course course = createCourseFromDTO(newCourseApp);
            assignRelationships(course, newCourseApp.teacherId(), newCourseApp.idSchedules());
            return new ResponseEntity<>("Course created!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), e.getMessage().contains("found")
                                                         ? HttpStatus.NOT_FOUND
                                                         : HttpStatus.FORBIDDEN);

        }
    }

    @Override
    public void validateCourseApp(NewCourseApplicationDTO newCourseApp) {
        validateExistsTeacher(newCourseApp.teacherId());
        validateSchedules(newCourseApp.idSchedules());
    }

    @Override
    public void validateExistsTeacher(String teacherId) {
        try {
            if (!teacherService.existsTeacherById(teacherId)) {
                throw validationException("Teacher not found");
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void validateSchedules(Set<String> idSchedules) {
        if (idSchedules != null) {
            validateLengthSetIdSchedules(idSchedules);
            validationsSchedulesByFor(idSchedules);
        }
    }

    @Override
    public void validateLengthSetIdSchedules(Set<String> idSchedules) {
        if (idSchedules.size() > 2) {
            throw  validationException("Courses can only have two schedules.");
        }
    }

    @Override
    public void validationsSchedulesByFor(Set<String> idSchedules) {
        for (String scheduleId: idSchedules) {
            validateExistsSchedule(scheduleId);
            validateQuantityCoursesInSchedule(scheduleId);
        }
    }

    @Override
    public void validateExistsSchedule(String scheduleId) {
        if (!scheduleService.existsScheduleById(scheduleId)) {
            throw validationException("One of the schedules could not be found");
        }
    }

    @Override
    public void validateQuantityCoursesInSchedule(String scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        if (courseScheduleService.countCourseScheduleBySchedule(schedule) >= 5) {
            throw validationException("There can only be five simultaneous courses per schedule.");
        }
    }

    @Override
    public Course createCourseFromDTO(NewCourseApplicationDTO newCourseApp) {
        return new Course(newCourseApp.name(), newCourseApp.place());
    }

    @Override
    public Set<Schedule> findSchedules(Set<String> idSchedules) {
        Set<Schedule> schedules = new HashSet<>();
        addSchedulesToSet(schedules, idSchedules);
        return schedules;
    }

    @Override
    public void addSchedulesToSet(Set<Schedule> schedules, Set<String> idSchedules) {
        for (String scheduleId : idSchedules) {
            schedules.add(scheduleService.getScheduleById(scheduleId));
        }
    }

    @Override
    public void assignRelationships(Course course, String teacherId, Set<String> idSchedules) {
        assignTeacherToCourse(course, teacherId);
        if (idSchedules != null) {
            assignSchedulesToCourse(course, findSchedules(idSchedules));
        }
    }

    @Override
    public void assignTeacherToCourse(Course course, String teacherId) {
        course.setTeacher(teacherId != null
                ? teacherService.getTeacherById(teacherId)
                : null);
    }

    @Override
    public void assignSchedulesToCourse(Course course, Set<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            courseScheduleService.createNewCourseSchedule(course, schedule);
        }
    }

}
