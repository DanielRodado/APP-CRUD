package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.repositories.AdminRepository;
import com.mindhub.AppCrud.services.AdminService;
import com.mindhub.AppCrud.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mindhub.AppCrud.utils.ValidationExceptionUtil.validationException;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PersonService personService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean existsAdminByEmail(String adminEmail) {
        return adminRepository.existsByEmail(adminEmail);
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    // Others methods

    @Override
    public Admin createAdminFromDTO(NewPersonApplicationDTO newAdminApp) {
        return new Admin(
                newAdminApp.firstName(),
                newAdminApp.lastName(),
                newAdminApp.email(),
                passwordEncoder.encode(newAdminApp.password())
        );
    }

    // Methods controllers

    // Create new Admin
    @Override
    public ResponseEntity<String> createNewAdmin(NewPersonApplicationDTO newAdminApp) {
        try {
            validateAdminApp(newAdminApp);
            Admin admin = createAdminFromDTO(newAdminApp);
            saveAdmin(admin);
            return new ResponseEntity<>("Admin created!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void validateAdminApp(NewPersonApplicationDTO newAdminApp) {
        personService.validateEmail(newAdminApp.email());
        personService.validateRequirementsEmail(newAdminApp.email(), "admin");
        validateUniqueEmail(newAdminApp.email());
        personService.validateFirstName(newAdminApp.firstName());
        personService.validateLatsName(newAdminApp.lastName());
    }

    @Override
    public void validateUniqueEmail(String adminEmail) {
        if (!existsAdminByEmail(adminEmail)) {
            throw validationException("This e-mail is registered");
        }
    }

}
