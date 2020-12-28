package com.EAMS.service.impl;

import com.EAMS.dao.impl.ClassesDaoImpl;
import com.EAMS.domain.Classes;
import com.EAMS.service.ClassesService;
import com.EAMS.util.JdbcUtil;

import java.sql.Connection;
import java.util.List;

public class ClassesServiceImpl implements ClassesService {
    Connection connection = JdbcUtil.getCon();
    ClassesDaoImpl classesDao = new ClassesDaoImpl(connection);

    @Override
    public int getClassNum() {
        return classesDao.getClassNum();
    }

    @Override
    public List<Classes> getClassListByCondition(String majorName, String grade) {
        return classesDao.getClassListByCondition(majorName, grade);
    }

    @Override
    public Classes getClassByCondition(String major, String year, String classNo) {
        return classesDao.getClassByCondition(major,year,classNo);
    }
}
