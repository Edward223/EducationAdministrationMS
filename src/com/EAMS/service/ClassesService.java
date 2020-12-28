package com.EAMS.service;

import com.EAMS.domain.Classes;

import java.util.List;

public interface ClassesService {
    int getClassNum();

    List<Classes> getClassListByCondition(String majorName, String grade);

    Classes getClassByCondition(String saveMajor, String saveYear, String saveClassNo);
}
