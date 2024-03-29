package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.DTO.StudentDTO;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.repositories.StudentRepository;
import com.mindhub.AppCrud.services.PersonService;
import com.mindhub.AppCrud.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.PersonUtil.verifyEmailByType;
import static com.mindhub.AppCrud.utils.ValidationExceptionUtil.validationException;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Methods repository

    @Override
    public Set<Student> getAllStudents() {
        return new HashSet<>(studentRepository.findAll());
    }

    @Override
    public Set<Student> getAllStudentsByFirstNameContainingIgnoreCase(String letter) {
        return studentRepository.findByFirstNameContainingIgnoreCase(letter);
    }

    @Override
    public Set<StudentDTO> getAllStudentsDTO() {
        return getAllStudents().stream().map(StudentDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<StudentDTO> getAllStudentsDTOByFirstNameContaining(String letter) {
        return getAllStudentsByFirstNameContainingIgnoreCase(letter).stream().map(StudentDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<StudentDTO> convertToCollectionToStudentsDTO(Set<Student> students) {
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

    // Methods controllers

    // Create new Student
    @Override
    public ResponseEntity<String> createNewStudent(NewPersonApplicationDTO newStudentApp) {
        try {
            validateStudentApp(newStudentApp);
            Student student = createStudentFromDTO(newStudentApp);
            saveStudent(student);
            return new ResponseEntity<>("Student created!", HttpStatus.CREATED);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void validateStudentApp(NewPersonApplicationDTO newStudentApp) {
        personService.validateEmail(newStudentApp.email());
        personService.validateRequirementsEmail(newStudentApp.email(), "student");
        validateUniqueEmail(newStudentApp.email());
        personService.validateFirstName(newStudentApp.firstName());
        personService.validateLatsName(newStudentApp.lastName());
        personService.validatePassword(newStudentApp.password());
    }

    @Override
    public void validateUniqueEmail(String email) {
        if (existsStudentByEmail(email)) {
            throw validationException("This e-mail is registered");
        }
    }

    @Override
    public Student createStudentFromDTO(NewPersonApplicationDTO newStudentApp) {
        return new Student(
                newStudentApp.firstName(),
                newStudentApp.lastName(),
                newStudentApp.email(),
                passwordEncoder.encode(newStudentApp.password())
        );
    }

    //  Find all Student if containing letter in first name
    @Override
    public ResponseEntity<Object> getAllStudentsDTOIfFirstNameContaining(String letter) {

        Set<Student> students = getAllStudentsByFirstNameContainingIgnoreCase(letter);

        return students.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : createResponseWithStudentsDTO(students);
    }

    @Override
    public ResponseEntity<Object> createResponseWithStudentsDTO(Set<Student> students) {
        return new ResponseEntity<>(convertToCollectionToStudentsDTO(students), HttpStatus.OK);
    }

}
