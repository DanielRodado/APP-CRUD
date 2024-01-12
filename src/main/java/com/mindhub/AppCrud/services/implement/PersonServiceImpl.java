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

import static com.mindhub.AppCrud.utils.PersonUtil.verifyEmailByType;
import static com.mindhub.AppCrud.utils.ValidationExceptionUtil.validationException;

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

    @Override
    public void validateEmail(String email) {
        if (email.isBlank()) {
            throw validationException("The mail cannot be empty");
        }
    }

    @Override
    public void validateFirstName(String firstName) {
        if (firstName.isBlank()) {
            throw validationException("The first name cannot be empty");
        }
    }

    @Override
    public void validateLatsName(String lastName) {
        if (lastName.isBlank()) {
            throw validationException("The last name cannot be empty");
        }
    }

    @Override
    public void validatePassword(String password) {
        if (password.isBlank()) {
            throw validationException("The password cannot be empty");
        }
    }

    @Override
    public void validateRequirementsEmail(String email, String emailType) {
        if (!verifyEmailByType(email, emailType)) {
            throw validationException("The email must have an '@'; '"+emailType+"', after the '@'; '.com', after '" +
                    emailType + "' and no characters after the '.com'");
        }
    }

}
