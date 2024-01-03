package com.mindhub.AppCrud.models.subClass;

import com.mindhub.AppCrud.models.Person;
import jakarta.persistence.Entity;

@Entity
public class Student extends Person {

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
}
