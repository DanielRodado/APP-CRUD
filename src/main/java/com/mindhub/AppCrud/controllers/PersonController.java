package com.mindhub.AppCrud.controllers;

import com.mindhub.AppCrud.DTO.PersonDTO;
import com.mindhub.AppCrud.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonService personServices;

    @GetMapping("/persons")
    public Set<PersonDTO> getAllPersonsDTO() {
        return personServices.getAllPersonsDTO();
    }

}
