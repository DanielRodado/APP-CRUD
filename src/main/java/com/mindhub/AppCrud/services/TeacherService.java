package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.models.subClass.Teacher;

import java.util.Set;

public interface TeacherService {

    Set<Teacher> getAllTeachers();

    Set<TeacherDTO> getAllTeachersDTO();

    Teacher getTeacherById(String teacherId);

    Teacher getTeacherByEmail(String teacherEmail);

    TeacherDTO getTeacherDTOById(String teacherId);

    TeacherDTO getTeacherDTOByEmail(String teacherEmail);

    boolean existsTeacherById(String teacherId);

    boolean existsTeacherByEmail(String teacherEmail);

    void saveTeacher(Teacher teacher);

}
