package com.mindhub.AppCrud.Controllers;

import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping("/teachers")
    public Set<TeacherDTO> getAllTeachersDTO() {
        return teacherRepository.findAll().stream().map(TeacherDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/teachers/current")
    public TeacherDTO getTeacherCurrent(Authentication authentication) {
        return new TeacherDTO(teacherRepository.findByEmail(authentication.getName()));
    }

}
