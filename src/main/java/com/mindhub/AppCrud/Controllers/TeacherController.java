package com.mindhub.AppCrud.Controllers;

import com.mindhub.AppCrud.DTO.NewTeacherApplicationDTO;
import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.CourseRepository;
import com.mindhub.AppCrud.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.TeacherUtil.verifyEmailTeacher;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/teachers")
    public Set<TeacherDTO> getAllTeachersDTO() {
        return teacherRepository.findAll().stream().map(TeacherDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/teachers/current")
    public TeacherDTO getTeacherCurrent(Authentication authentication) {
        return new TeacherDTO(teacherRepository.findByEmail(authentication.getName()));
    }

    @PostMapping("/teachers")
    public ResponseEntity<String> createNewTeacher(@RequestBody NewTeacherApplicationDTO newTeacherApp) {

        if (newTeacherApp.email().isBlank()) {
            return new ResponseEntity<>("The mail cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (teacherRepository.existsByEmail(newTeacherApp.email())) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (!verifyEmailTeacher(newTeacherApp.email())) {
            return new ResponseEntity<>("The email must have an '@'; 'mentor', after the '@'; '.com', after 'mentor';" +
                    " and no characters after the '.com'", HttpStatus.FORBIDDEN);
        }

        if (newTeacherApp.firstName().isBlank()) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (newTeacherApp.lastName().isBlank()) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (newTeacherApp.specializations().isEmpty()) {
            return new ResponseEntity<>("Enter at least one specialization", HttpStatus.FORBIDDEN);
        }

        Teacher teacher = new Teacher(newTeacherApp.firstName(), newTeacherApp.lastName(), newTeacherApp.email(),
                passwordEncoder.encode(newTeacherApp.password()), newTeacherApp.specializations());

        teacherRepository.save(teacher);

        return new ResponseEntity<>("Teacher created!", HttpStatus.CREATED);
    }

    @PatchMapping("/teachers/add/course")
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

}
