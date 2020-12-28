package com.EAMS.service;

import com.EAMS.domain.Classroom;

import java.util.List;

public interface ClassroomService {

    int getClassroomNum();

    List<Classroom> getClassroomList();

    String getClassroomIdByCondition(String classroomNo);

    Classroom getClassroomByCondition(String ClassroomNo);
}
