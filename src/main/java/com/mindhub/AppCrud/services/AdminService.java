package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.DTO.NewPersonApplicationDTO;
import com.mindhub.AppCrud.models.subClass.Admin;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    // Methods repository

    boolean existsAdminByEmail(String adminEmail);

    void saveAdmin(Admin admin);

    // Others methods

    Admin createAdminFromDTO(NewPersonApplicationDTO newAdminApp);

    // Methods controllers

    // Create new Admin
    ResponseEntity<String> createNewAdmin(NewPersonApplicationDTO newAdminApp);

    void validateAdminApp(NewPersonApplicationDTO newAdminApp);

    void validateUniqueEmail(String adminEmail);

}
