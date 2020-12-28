package com.EAMS.dao;

import com.EAMS.domain.Timeslot;

import java.util.List;

public interface TimeslotDao {
    Timeslot getTimeslotById(String timeslot);

    Timeslot getTimeslotByCondition(String day, String time);

    List<Timeslot> getTimeslotList();
}
