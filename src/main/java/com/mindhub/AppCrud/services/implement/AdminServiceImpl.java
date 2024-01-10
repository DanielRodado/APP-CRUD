package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.repositories.AdminRepository;
import com.mindhub.AppCrud.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public boolean existsAdminByEmail(String adminEmail) {
        return adminRepository.existsByEmail(adminEmail);
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

}
