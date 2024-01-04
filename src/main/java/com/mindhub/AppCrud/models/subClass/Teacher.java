package com.mindhub.AppCrud.models.subClass;

import com.mindhub.AppCrud.models.Person;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Teacher extends Person {

    // Properties

    @ElementCollection
    private List<String> specializations;

   // Constructor methods

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String email, String password, List<String> specialization) {
        super(firstName, lastName, email, password);
        this.specializations = specialization;
    }

    public List<String> getSpecialization() {
        return specializations;
    }

    public void setSpecialization(List<String> specializations) {
        this.specializations = specializations;
    }
}
