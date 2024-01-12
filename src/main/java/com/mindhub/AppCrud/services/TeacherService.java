package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.NewTeacherApplicationDTO;
import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.models.subClass.Teacher;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface TeacherService {

    // Methods repository

    Set<Teacher> getAllTeachers();

    Set<TeacherDTO> getAllTeachersDTO();

    Teacher getTeacherById(String teacherId);

    Teacher getTeacherByEmail(String teacherEmail);

    TeacherDTO getTeacherDTOById(String teacherId);

    TeacherDTO getTeacherDTOByEmail(String teacherEmail);

    boolean existsTeacherById(String teacherId);

    boolean existsTeacherByEmail(String teacherEmail);

    void saveTeacher(Teacher teacher);

    // Methods controllers

    // Create new Teacher
    ResponseEntity<String> createNewTeacher(NewTeacherApplicationDTO newTeacherApp);

    void validateTeacherApp(NewTeacherApplicationDTO newTeacherApp);

    void validateUniqueEmail(String email);

    void validatesSpecializations(List<String> specializations);

    Teacher createTeacherFromDTO(NewTeacherApplicationDTO newTeacherApp);

}
