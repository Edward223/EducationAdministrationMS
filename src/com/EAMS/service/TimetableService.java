package com.EAMS.service;

import com.EAMS.domain.Timeslot;
import com.EAMS.domain.Timetable;
import com.EAMS.exception.ArrangeCourseException;
import com.EAMS.vo.TimetableVo;

import java.util.List;
import java.util.Map;

public interface TimetableService {
    List<TimetableVo> getTimetableVoList(Map<String, String> map);

    boolean deleteById(String id);

    boolean init();

    Timeslot getTimeslotByCondition(String day, String time);

    boolean saveTimetable(Timetable timetable) throws ArrangeCourseException;

    void autoArrangeCourse();

    Map<String, Integer> getWorkTimeByTeacher(String week, String tName);

    Map<String, Integer> getWorkTime(String week);
}
