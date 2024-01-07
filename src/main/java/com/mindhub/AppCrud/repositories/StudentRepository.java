package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.subClass.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StudentRepository extends JpaRepository<Student, String> {

    Student findByEmail(String email);

    boolean existsByEmail(String email);

}
