package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.subClass.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdminRepository extends JpaRepository<Admin, String> {
}
