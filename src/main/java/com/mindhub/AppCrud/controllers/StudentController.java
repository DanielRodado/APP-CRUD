package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.services.StudentService;
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
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/students")
    public Set<StudentDTO> getAllStudentDTO() {
        return studentService.getAllStudentsDTO();
    }

    @GetMapping("/students/first-name/containing/{letter}")
    public ResponseEntity<Object> getAllStudentsDTOIfFirstNameContaining(@PathVariable String letter) {

        Set<Student> students = studentService.getAllStudentsIfFirstNameContainingIgnoreCase(letter);

        return students.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(studentService.convertToCollectionStudentsDTO(students), HttpStatus.OK);

    }

    @PostMapping("/students")
    public ResponseEntity<String> createNewStudent(@RequestBody NewPersonApplicationDTO newStudentApp) {

        if (newStudentApp.email().isBlank()) {
            return new ResponseEntity<>("The mail cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (!verifyEmailByType(newStudentApp.email(), "student")) {
            return new ResponseEntity<>("The email must have an '@'; 'student', after the '@'; '.com', after 'student';" +
                    " and no characters after the '.com'", HttpStatus.FORBIDDEN);
        }

        if (studentService.existsStudentByEmail(newStudentApp.email().toLowerCase())) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (newStudentApp.firstName().isBlank()) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        if (newStudentApp.lastName().isBlank()) {
            return new ResponseEntity<>("This e-mail is registered", HttpStatus.FORBIDDEN);
        }

        Student student = new Student(newStudentApp.firstName(), newStudentApp.lastName(), newStudentApp.email().toLowerCase(),
                passwordEncoder.encode(newStudentApp.password()));
        studentService.saveStudent(student);

        return new ResponseEntity<>("Student created!", HttpStatus.CREATED);

    }

    @GetMapping("/students/current")
    public StudentDTO getStudentCurrent(Authentication studentCurrent) {
        return studentService.getStudentDTOByEmail(studentCurrent.getName());
    }

}
