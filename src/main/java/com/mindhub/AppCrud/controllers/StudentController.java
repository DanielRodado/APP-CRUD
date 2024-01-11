package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public Set<StudentDTO> getAllStudentDTO() {
        return studentService.getAllStudentsDTO();
    }

    @GetMapping("/students/current")
    public StudentDTO getStudentCurrent(Authentication studentCurrent) {
        return studentService.getStudentDTOByEmail(studentCurrent.getName());
    }

    @GetMapping("/students/first-name/containing/{letter}")
    public ResponseEntity<Object> getAllStudentsDTOIfFirstNameContaining(@PathVariable String letter) {
        return studentService.getAllStudentsDTOIfFirstNameContaining(letter);
    }

    @PostMapping("/students")
    public ResponseEntity<String> createNewStudent(@RequestBody NewPersonApplicationDTO newStudentApp) {
        return studentService.createNewStudent(newStudentApp);
    }

}
