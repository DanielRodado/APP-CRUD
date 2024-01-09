package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.PersonDTO;
import com.mindhub.AppCrud.models.Person;

import java.util.Set;

public interface PersonServices {

    Set<Person> getAllPersons();

    Set<PersonDTO> getAllPersonsDTO();

}
