package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.PersonDTO;
import com.mindhub.AppCrud.models.Person;
import com.mindhub.AppCrud.repositories.PersonRepository;
import com.mindhub.AppCrud.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Set<Person> getAllPersons() {
        return new HashSet<>(personRepository.findAll());
    }

    @Override
    public Set<PersonDTO> getAllPersonsDTO() {
        return getAllPersons().stream().map(PersonDTO::new).collect(Collectors.toSet());
    }

}
