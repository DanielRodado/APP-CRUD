package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.models.subClass.Student;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface StudentService {

    // Methods repository

    Set<Student> getAllStudents();

    Set<Student> getAllStudentsByFirstNameContainingIgnoreCase(String letter);

    Set<StudentDTO> getAllStudentsDTO();

    Set<StudentDTO> getAllStudentsDTOByFirstNameContaining(String letter);

    Set<StudentDTO> convertToCollectionToStudentsDTO(Set<Student> students);

    Student getStudentById(String studentId);

    Student getStudentByEmail(String studentEmail);

    StudentDTO getStudentDTOByEmail(String studentEmail);

    boolean existsStudentById(String studentId);

    boolean existsStudentByEmail(String studentEmail);

    void saveStudent(Student student);

    // Methods controllers

    // Create new Student
    ResponseEntity<String> createNewStudent(NewPersonApplicationDTO newStudentApp);

    void validateStudentApp(NewPersonApplicationDTO newStudentApp);

    void validateEmail(String email);

    void validateFirstName(String firstName);

    void validateLatsName(String lastName);

    void validatePassword(String password);

    void validateUniqueEmail(String email);

    void validateRequirementsEmail(String email);

    Student createStudentFromDTO(NewPersonApplicationDTO newStudentApp);

    //  Find all Student if containing letter in first name
    ResponseEntity<Object> getAllStudentsDTOIfFirstNameContaining(String letter);

    ResponseEntity<Object> createResponseWithStudentsDTO(Set<Student> students);

}
