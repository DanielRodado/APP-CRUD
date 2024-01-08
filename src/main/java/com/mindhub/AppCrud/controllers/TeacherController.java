package com.mindhub.AppCrud.controllers;

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

import static com.mindhub.AppCrud.utils.PersonUtil.verifyEmailByType;

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

        if (!verifyEmailByType(newTeacherApp.email(), "mentor")) {
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

}
