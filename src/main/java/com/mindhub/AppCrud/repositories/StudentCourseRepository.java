package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StudentCourseRepository extends JpaRepository<StudentCourse, String> {
}
