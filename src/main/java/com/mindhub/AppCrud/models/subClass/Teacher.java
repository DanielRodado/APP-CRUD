package com.mindhub.AppCrud.models.subClass;

import com.mindhub.AppCrud.models.Person;
import jakarta.persistence.Entity;

@Entity
public class Teacher extends Person {

   // Constructor methods

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

}
