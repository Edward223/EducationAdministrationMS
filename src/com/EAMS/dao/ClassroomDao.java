package com.EAMS.dao;

import com.EAMS.domain.Classroom;

import java.util.List;
import java.util.Map;

public interface ClassroomDao {
    int getClassNum();

    List<Classroom> getClassroomList();

    Classroom getClassroomByCondition(Map<String, String> map);

    Classroom getClassroomById(String classroomId);

    List<Classroom> getClassroomListByHoldNum(String studentNum);
}
