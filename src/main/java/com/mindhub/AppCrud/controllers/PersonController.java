package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.PersonDTO;
import com.mindhub.AppCrud.repositories.PersonRepository;
import com.mindhub.AppCrud.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @GetMapping("/persons")
    public Set<PersonDTO> getAllPersonsDTO() {
        return personServices.getAllPersonsDTO();
    }

}
