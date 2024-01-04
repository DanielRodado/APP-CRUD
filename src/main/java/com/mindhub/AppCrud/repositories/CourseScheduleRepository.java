package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, String> {
}
