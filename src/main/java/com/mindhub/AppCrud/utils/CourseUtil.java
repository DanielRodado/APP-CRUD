package com.mindhub.AppCrud.utils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public final class CourseUtil {

    public static boolean checkRangeOfHours(LocalTime startTime, LocalTime endTime, int rangeOfDifference) {
        return Math.abs(ChronoUnit.HOURS.between(startTime, endTime)) < rangeOfDifference;
    }

}
