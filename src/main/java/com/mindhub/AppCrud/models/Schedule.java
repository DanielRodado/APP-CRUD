package com.mindhub.AppCrud.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Schedule {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String dayWeek, startTime, timeEnd;

    // Constructor methods

    public Schedule() {
    }

    public Schedule(String dayWeek, String startTime, String timeEnd) {
        this.dayWeek = dayWeek;
        this.startTime = startTime;
        this.timeEnd = timeEnd;
    }

    // Accessory methods

    public String getId() {
        return id;
    }

    public String getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(String dayWeek) {
        this.dayWeek = dayWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
