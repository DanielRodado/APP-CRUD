package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.subClass.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface CourseRepository extends JpaRepository<Course, String> {

    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.teacher = :teacherParam WHERE c.id = :courseId")
    void addCourseToTeacherById(@Param("teacherParam") Teacher teacher, @Param("courseId") String courseId);

    boolean existsByIdAndTeacherIsNotNull(String courseId);

}
