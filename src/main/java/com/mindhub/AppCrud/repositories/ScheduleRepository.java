package com.mindhub.AppCrud.repositories;

import com.mindhub.AppCrud.models.DayType;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalTime;

@RepositoryRestResource
public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    boolean existsByDayWeekAndShiftTypeAndStartTimeAndTimeEnd(DayType dayWeek, ShiftType shiftType,
                                                              LocalTime startTime, LocalTime timeEnd);

}
