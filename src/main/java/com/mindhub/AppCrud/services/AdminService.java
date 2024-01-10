package com.mindhub.AppCrud.services;

import com.mindhub.AppCrud.models.subClass.Admin;

public interface AdminService {

    boolean existsAdminByEmail(String adminEmail);

    void saveAdmin(Admin admin);

}
