package com.EAMS.dao;

import com.EAMS.domain.Classes;

import java.util.List;

public interface ClassesDao {
    int getClassNum();

    List<Classes> getClassListByCondition(String majorName, String grade);

    Classes getClassByCondition(String major, String year, String classNo);

    Classes getClassById(String classId);
}
