package com.mindhub.AppCrud.DTO;

import java.time.LocalTime;

public record NewScheduleApplicationDTO(String dayWeek, String shiftType, LocalTime startTime, LocalTime timeEnd) {
}
