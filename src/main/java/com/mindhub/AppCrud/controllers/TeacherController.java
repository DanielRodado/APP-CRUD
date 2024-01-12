package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewTeacherApplicationDTO;
import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.mindhub.AppCrud.utils.PersonUtil.verifyEmailByType;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teachers")
    public Set<TeacherDTO> getAllTeachersDTO() {
        return teacherService.getAllTeachersDTO();
    }

    @GetMapping("/teachers/current")
    public TeacherDTO getTeacherCurrent(Authentication teacherCurrent) {
        return teacherService.getTeacherDTOByEmail(teacherCurrent.getName());
    }

    @PostMapping("/teachers")
    public ResponseEntity<String> createNewTeacher(@RequestBody NewTeacherApplicationDTO newTeacherApp) {
        return teacherService.createNewTeacher(newTeacherApp);
    }

}
