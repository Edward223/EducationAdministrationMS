package com.EAMS.service.impl;

import com.EAMS.dao.impl.ClassroomDaoImpl;
import com.EAMS.domain.Classroom;
import com.EAMS.service.ClassroomService;
import com.EAMS.util.JdbcUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassroomServiceImpl implements ClassroomService {
    Connection connection = JdbcUtil.getCon();
    ClassroomDaoImpl classroomDao = new ClassroomDaoImpl(connection);

    @Override
    public int getClassroomNum() {
        return classroomDao.getClassNum();
    }

    @Override
    public List<Classroom> getClassroomList() {
        return classroomDao.getClassroomList();
    }

    @Override
    public String getClassroomIdByCondition(String classroomNo) {
        Map<String,String> map = new HashMap<>();
        map.put("area",classroomNo.substring(0,2));
        map.put("floor",String.valueOf(classroomNo.charAt(3)));
        map.put("roomNo",classroomNo.substring(4));
        Classroom classroom =  classroomDao.getClassroomByCondition(map);
        return classroom.getId();
    }

    @Override
    public Classroom getClassroomByCondition(String classroomNo) {
        Map<String,String> map = new HashMap<>();
        map.put("area",classroomNo.substring(0,2));
        map.put("floor",String.valueOf(classroomNo.charAt(3)));
        map.put("roomNo",classroomNo.substring(4));
        Classroom classroom =  classroomDao.getClassroomByCondition(map);
        return classroom;
    }
}
