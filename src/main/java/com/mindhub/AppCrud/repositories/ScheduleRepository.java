package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}
