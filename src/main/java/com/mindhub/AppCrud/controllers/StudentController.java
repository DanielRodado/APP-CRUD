package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/students")
    public Set<StudentDTO> getAllStudentDTO() {
        return studentRepository.findAll().stream().map(StudentDTO::new).collect(Collectors.toSet());
    }

}
