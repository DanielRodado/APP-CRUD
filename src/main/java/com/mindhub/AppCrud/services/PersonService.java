package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.PersonDTO;
import com.mindhub.AppCrud.models.Person;

import java.util.Set;

public interface PersonService {

    // Methods repository

    Set<Person> getAllPersons();

    Set<PersonDTO> getAllPersonsDTO();

    // Methods controllers

    // Create new Person - Validations
    void validateFirstName(String firstName);

    void validateLatsName(String lastName);

    void validateEmail(String email);

    void validatePassword(String password);

    void validateRequirementsEmail(String email, String emailType);

}
