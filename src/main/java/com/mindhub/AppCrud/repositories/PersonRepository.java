package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
