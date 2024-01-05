package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.subClass.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TeacherRepository extends JpaRepository<Teacher, String> {

    Teacher findByEmail(String email);

    boolean existsByEmail(String email);

}
