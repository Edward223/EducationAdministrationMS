package com.EAMS.dao;

import com.EAMS.domain.Timetable;
import com.EAMS.vo.TimetableVo;

import java.util.List;
import java.util.Map;

public interface TimetableDao {
    List<TimetableVo> getTimetableVoList(Map<String, String> map) ;

    int deleteById(String id);

    int getTimetableNum();

    int init();

    String getWeeksByRoomAndTimeslot(String classroomId, String timeslot);

    String getWeeksByTeacherAndTimeslot(String teacherId, String timeslot);

    int save(Timetable timetable);

    String getWeeksByClassesAndTimeslot(String classId, String timeslot);

    int getTimetableByClassAndCourse(String classId, String courseId);

    Map<String, Integer> getWorkTime(String week);

    Map<String, Integer> getWorkTimeByTeacher(String week, String tName);
}
