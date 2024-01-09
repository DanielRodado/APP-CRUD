package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.TeacherDTO;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.TeacherRepository;
import com.mindhub.AppCrud.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

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
    public boolean existsTeacherByEmail(String teacherEmail) {
        return teacherRepository.existsByEmail(teacherEmail);
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

}
