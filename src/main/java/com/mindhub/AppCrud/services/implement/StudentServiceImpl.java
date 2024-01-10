package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.StudentRepository;
import com.mindhub.AppCrud.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Set<Student> getAllStudents() {
        return new HashSet<>(studentRepository.findAll());
    }

    @Override
    public Set<Student> getAllStudentsIfFirstNameContainingIgnoreCase(String letter) {
        return studentRepository.findByFirstNameContainingIgnoreCase(letter);
    }

    @Override
    public Set<StudentDTO> getAllStudentsDTO() {
        return getAllStudents().stream().map(StudentDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<StudentDTO> getAllStudentsDTOIfFirstNameContaining(String letter) {
        return getAllStudentsIfFirstNameContainingIgnoreCase(letter).stream().map(StudentDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<StudentDTO> convertToCollectionStudentsDTO(Set<Student> students) {
        return students.stream().map(StudentDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public Student getStudentByEmail(String studentEmail) {
        return studentRepository.findByEmail(studentEmail);
    }

    @Override
    public StudentDTO getStudentDTOByEmail(String studentEmail) {
        return new StudentDTO(getStudentByEmail(studentEmail));
    }

    @Override
    public boolean existsStudentById(String studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public boolean existsStudentByEmail(String studentEmail) {
        return studentRepository.existsByEmail(studentEmail);
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }
}
