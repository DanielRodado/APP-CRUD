package com.mindhub.AppCrud.utils;

import com.mindhub.AppCrud.models.ShiftType;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public final class ScheduleUtil {

    /**
     * This method receives three arguments to compare and know if it meets a time difference between the first two arguments.
     * @param endTime The first argument is the start time.
     * @param startTime The second argument is the end time.
     * @param rangeOfDifference The third and last parameter is an integer with which you want to know if they comply with the
     *                time difference.
     * @return Returns a boolean; if it meets the time difference (of the last argument) a 'true', otherwise a 'false'.
     * */
    public static boolean checkRangeOfHours(LocalTime startTime, LocalTime endTime, int rangeOfDifference) {
        return Math.abs(ChronoUnit.HOURS.between(startTime, endTime)) < rangeOfDifference;
    }

    public static boolean checkRangeHourWithShiftType(LocalTime time, ShiftType shiftType) {

        if (time.isBefore(LocalTime.of(12, 0)) && shiftType.toString().equals("MORNING")) return true;

        if (time.isBefore(LocalTime.of(18, 30)) && shiftType.toString().equals("AFTERNOON")) return true;

        return time.isBefore(LocalTime.of(21, 30)) && shiftType.toString().equals("EVENING");

    }

}
