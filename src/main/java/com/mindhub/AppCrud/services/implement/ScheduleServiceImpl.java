package com.mindhub.AppCrud.services.implement;

import com.mindhub.AppCrud.DTO.ScheduleDTO;
import com.mindhub.AppCrud.models.DayType;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.ShiftType;
import com.mindhub.AppCrud.repositories.ScheduleRepository;
import com.mindhub.AppCrud.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public Set<Schedule> getAllSchedules() {
        return new HashSet<>(scheduleRepository.findAll());
    }

    @Override
    public Set<ScheduleDTO> getAllSchedulesDTO() {
        return getAllSchedules().stream().map(ScheduleDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Schedule getScheduleById(String scheduleId) {
        return scheduleRepository.findById(scheduleId).orElse(null);
    }

    @Override
    public boolean existsScheduleById(String scheduleId) {
        return scheduleRepository.existsById(scheduleId);
    }

    @Override
    public boolean existsSchedule(DayType dayWeek, ShiftType shiftType, LocalTime startTime, LocalTime endTime) {
        return scheduleRepository.existsByDayWeekAndShiftTypeAndStartTimeAndEndTime(dayWeek, shiftType, startTime, endTime);
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}
