package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalTime;
import java.util.Set;

@RepositoryRestResource
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, String> {

    @Query("SELECT cs.course FROM CourseSchedule cs WHERE cs.schedule.startTime BETWEEN :start AND :end")
    Set<Course> findCoursesByScheduleStartTimeBetween(@Param("start") LocalTime startTime,
                                                      @Param("end") LocalTime endTime);

    @Query("SELECT cs.course FROM CourseSchedule cs WHERE cs.schedule.endTime BETWEEN :start AND :end")
    Set<Course> findCoursesByScheduleEndTimeBetween(@Param("start") LocalTime startTime,
                                                      @Param("end") LocalTime endTime);

    byte countBySchedule(Schedule schedule);

}
