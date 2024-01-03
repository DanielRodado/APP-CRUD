package com.mindhub.AppCrud.models.subClass;

import com.mindhub.AppCrud.models.Person;
import jakarta.persistence.Entity;

@Entity
public class Admin extends Person {

    public Admin() {
    }

    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

}
