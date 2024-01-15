package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RepositoryRestResource
public interface StudentCourseRepository extends JpaRepository<StudentCourse, String> {

    boolean existsByStudentAndCourse(Student student, Course course);

    byte countByCourseAndIsActive(Course course, Boolean isActive);

    @Transactional
    @Modifying
    @Query("UPDATE StudentCourse sc SET isActive = false WHERE sc.student = :studentParam AND sc.course = :courseParam")
    void softDeleteStudentCourse(@Param("studentParam") Student student, @Param("courseParam") Course course);

}
