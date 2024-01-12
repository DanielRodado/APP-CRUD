package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.NewTeacherApplicationDTO;
import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.TeacherRepository;
import com.mindhub.AppCrud.services.PersonService;
import com.mindhub.AppCrud.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.AppCrud.utils.ValidationExceptionUtil.validationException;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Set<Teacher> getAllTeachers() {
        return new HashSet<>(teacherRepository.findAll());
    }

    @Override
    public Set<TeacherDTO> getAllTeachersDTO() {
        return getAllTeachers().stream().map(TeacherDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Teacher getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId).orElse(null);
    }

    @Override
    public Teacher getTeacherByEmail(String teacherEmail) {
        return teacherRepository.findByEmail(teacherEmail);
    }

    @Override
    public TeacherDTO getTeacherDTOById(String teacherId) {
        return new TeacherDTO(getTeacherById(teacherId));
    }

    @Override
    public TeacherDTO getTeacherDTOByEmail(String teacherEmail) {
        return new TeacherDTO (getTeacherByEmail(teacherEmail));
    }

    @Override
    public boolean existsTeacherById(String teacherId) {
        return teacherRepository.existsById(teacherId);
    }

    @Override
    public boolean existsTeacherByEmail(String teacherEmail) {
        return teacherRepository.existsByEmail(teacherEmail);
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public ResponseEntity<String> createNewTeacher(NewTeacherApplicationDTO newTeacherApp) {
        try {
            validateTeacherApp(newTeacherApp);
            Teacher teacher = createTeacherFromDTO(newTeacherApp);
            saveTeacher(teacher);
            return new ResponseEntity<>("Teacher created!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void validateTeacherApp(NewTeacherApplicationDTO newTeacherApp) {
        personService.validateEmail(newTeacherApp.email());
        personService.validateRequirementsEmail(newTeacherApp.email(), "teacher");
        validateUniqueEmail(newTeacherApp.email());
        validatesSpecializations(newTeacherApp.specializations());
        personService.validateFirstName(newTeacherApp.firstName());
        personService.validateLatsName(newTeacherApp.lastName());
        personService.validatePassword(newTeacherApp.password());
    }

    @Override
    public void validateUniqueEmail(String email) {
        if (existsTeacherByEmail(email)) {
            throw validationException("This e-mail is registered.");
        }
    }

    @Override
    public void validatesSpecializations(List<String> specializations) {
        if (specializations.isEmpty()) {
            throw validationException("Enter at least one specialization.");
        }
    }

    @Override
    public Teacher createTeacherFromDTO(NewTeacherApplicationDTO newTeacherApp) {
        return new Teacher(
                newTeacherApp.firstName(),
                newTeacherApp.lastName(),
                newTeacherApp.email(),
                passwordEncoder.encode(newTeacherApp.password()),
                newTeacherApp.specializations()
        );
    }

}
