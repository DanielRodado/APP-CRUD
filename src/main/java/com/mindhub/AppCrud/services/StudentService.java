package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.models.subClass.Student;

import java.util.Set;

public interface StudentService {

    Set<Student> getAllStudents();

    Set<Student> getAllStudentsIfFirstNameContainingIgnoreCase(String letter);

    Set<StudentDTO> getAllStudentsDTO();

    Set<StudentDTO> getAllStudentsDTOIfFirstNameContaining(String letter);

    Set<StudentDTO> convertToCollectionStudentsDTO(Set<Student> students);

    Student getStudentById(String studentId);

    Student getStudentByEmail(String studentEmail);

    StudentDTO getStudentDTOByEmail(String studentEmail);

    boolean existsStudentById(String studentId);

    boolean existsStudentByEmail(String studentEmail);

    void saveStudent(Student student);

}
