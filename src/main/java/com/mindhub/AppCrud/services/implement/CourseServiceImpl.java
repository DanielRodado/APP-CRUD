package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.CourseDTO;
import com.mindhub.AppCrud.DTO.NewCourseApplicationDTO;
import com.mindhub.AppCrud.DTO.RecordStudentCourse;
import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.CourseRepository;
import com.mindhub.AppCrud.services.*;
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
    private StudentService studentService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @Autowired
    private StudentCourseService studentCourseService;

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

    // Others methods

    @Override
    public Set<Course> getCoursesFromStudentCourse(Set<StudentCourse> studentCourses) {
        return studentCourses.stream().map(StudentCourse::getCourse).collect(Collectors.toSet());
    }


    // Methods controllers

    public HttpStatus getHttpStatusByCondition(String text) {
        return text.toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN;
    }

    // Create new Course
    @Override
    public ResponseEntity<String> createNewCourse(NewCourseApplicationDTO newCourseApp) {
        try {
            validateCourseApp(newCourseApp);
            Course course = createCourseFromDTO(newCourseApp);
            saveCourse(course);
            assignRelationships(course, newCourseApp.teacherId(), newCourseApp.idSchedules());
            saveCourse(course);
            return new ResponseEntity<>("Course created!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), getHttpStatusByCondition(e.getMessage()));

        }
    }

    @Override
    public void validateCourseApp(NewCourseApplicationDTO newCourseApp) {
        validateName(newCourseApp.name());
        validatePlace(newCourseApp.place());
        validateExistsTeacherById(newCourseApp.teacherId());
        validateSchedules(newCourseApp.idSchedules());
    }

    @Override
    public void validateName(String name) {
        if (name.isBlank()) {
            throw validationException("The name cannot be empty.");
        }
    }

    @Override
    public void validatePlace(String place) {
        if (place.isBlank()) {
            throw validationException("The place cannot be empty.");
        }
    }

    @Override
    public void validateExistsTeacherById(String teacherId) {
        if (teacherId != null) {
            if (!teacherService.existsTeacherById(teacherId)) {
                throw validationException("Teacher not found");
            }
        }
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
            throw validationException("Courses can only have two schedules.");
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

    // Remove course from Teacher
    @Override
    public ResponseEntity<String> removeCourseFromTeacher(String teacherEmail, String courseId) {
        try {
            validationsBeforeRemoveCourseFromTeacher(teacherEmail, courseId);
            removeTeacherFromCourseById(courseId);
            return new ResponseEntity<>("Course removed!", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), getHttpStatusByCondition(e.getMessage()));
        }
    }

    @Override
    public void validationsBeforeRemoveCourseFromTeacher(String teacherEmail, String courseId) {
        validateExistsCourseById(courseId);
        validateCourseInTeacher(courseId, teacherEmail);
    }

    @Override
    public void validateExistsCourseById(String courseId) {
        if (!existsCourseById(courseId)) {
            throw validationException("Course not found.");
        }
    }

    @Override
    public void validateCourseInTeacher(String courseId, String teacherEmail) {
        if (!existsCourseByIdAndTeacher(courseId, teacherService.getTeacherByEmail(teacherEmail))) {
            throw validationException("The course does not belong to the teacher.");
        }
    }

    @Override
    public ResponseEntity<String> addCourseToStudent(String studentEmail, String courseId) {
        try {
            StudentCourse studentCourse = validationsBeforeAddCourseToStudent(studentEmail, courseId);
            studentCourseService.saveStudentCourse(studentCourse);
            return new ResponseEntity<>("Course added to student!", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), getHttpStatusByCondition(e.getMessage()));
        }
    }

    @Override
    public StudentCourse validationsBeforeAddCourseToStudent(String studentEmail, String courseId) {
        validateExistsCourseById(courseId);
        StudentCourse studentCourse =
                studentCourseService.createNewStudentCourse(studentService.getStudentByEmail(studentEmail), getCourseById(courseId));
        validateExistsStudentInCourse(studentCourse.getStudent(), studentCourse.getCourse());
        validateQuantityStudentInCourse(studentCourse.getCourse());
        return studentCourse;
    }

    @Override
    public void validateExistsStudentInCourse(Student student, Course course) {
        if (studentCourseService.existsStudentCourseByStudentAndCourse(student, course)) {
            throw validationException("The student is already in the course.");
        }
    }

    @Override
    public void validateQuantityStudentInCourse(Course course) {
        if (studentCourseService.countStudentCourseByCourseAndIsActive(course, true) > 20) {
            throw validationException("The maximum capacity of a course is 20 students.");
        }
    }

    @Override
    public ResponseEntity<String> removeCourseFromStudent(String studentEmail, String courseId) {
        try {
            RecordStudentCourse studentCourse = validationsBeforeRemoveCourseFromStudent(studentEmail, courseId);
            studentCourseService.softDeleteStudentCourse(studentCourse.student(), studentCourse.course());
            return new ResponseEntity<>("Course removed!", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), getHttpStatusByCondition(e.getMessage()));
        }
    }

    @Override
    public RecordStudentCourse validationsBeforeRemoveCourseFromStudent(String studentEmail, String courseId) {
        validateExistsCourseById(courseId);
        RecordStudentCourse studentCourse = createNewRecordStudentCourseByEmailAndId(studentEmail, courseId);
        validateNotExistsStudentInCourse(studentCourse.student(), studentCourse.course());
        return studentCourse;
    }

    @Override
    public void validateNotExistsStudentInCourse(Student student, Course course) {
        if (!studentCourseService.existsStudentCourseByStudentAndCourse(student, course)) {
            throw validationException("The student is not in the course.");
        }
    }

    @Override
    public RecordStudentCourse createNewRecordStudentCourseByEmailAndId(String studentEmail, String courseId) {
        return new RecordStudentCourse(studentService.getStudentByEmail(studentEmail), getCourseById(courseId));
    }


}
