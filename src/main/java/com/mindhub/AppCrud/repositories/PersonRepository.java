package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PersonRepository extends JpaRepository<Person, String> {

    Person findByEmail(String email);

}
