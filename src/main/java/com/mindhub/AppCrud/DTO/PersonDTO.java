package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.Person;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Teacher;

public class PersonDTO {

    // Properties

    private final String id, firstName, lastName, email, role;

    // Constructor methods

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.role = person instanceof Admin ? "Admin" : person instanceof Teacher ? "Teacher" : "Student";
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
