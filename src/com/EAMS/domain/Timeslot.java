package com.EAMS.domain;

public class Timeslot {
    private String id;
    private String day;
    private String time;

    public Timeslot() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return day +"-"+ time;
    }
}
