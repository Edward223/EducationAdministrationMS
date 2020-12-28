package com.EAMS.service.impl;

import com.EAMS.dao.impl.TeacherDaoImpl;
import com.EAMS.domain.Course;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.TeacherCourseRelation;
import com.EAMS.service.TeacherService;
import com.EAMS.util.JdbcUtil;
import com.EAMS.vo.TeacherVo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TeacherServiceImpl implements TeacherService {
    Connection connection = JdbcUtil.getCon();
    TeacherDaoImpl teacherDao = new TeacherDaoImpl(connection);

    @Override
    public int getTeacherNum() {
        return teacherDao.getTeacherNum();
    }

    @Override
    public List<TeacherVo> getTeacherVoList(Map<String, String> map) {
        return teacherDao.getTeacherVoList(map);
    }

    @Override
    public boolean deleteById(String id) {
        boolean flag = true;
        try {
            teacherDao.getConnection().setAutoCommit(false);
            int result1 = teacherDao.deleteById(id);
            if(result1 != 1){
                flag = false;
                teacherDao.getConnection().rollback();
            }
            int courseNum = teacherDao.getCourseNumByTid(id);
            int result2 = teacherDao.unBundCourses(id);
            if(result2 != courseNum){
                flag = false;
                teacherDao.getConnection().rollback();
            }
            teacherDao.getConnection().commit();
            teacherDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean update(Teacher teacher) {
        boolean flag = true;
        try {
            teacherDao.getConnection().setAutoCommit(false);
            int result = teacherDao.update(teacher);
            if(result != 1){
                flag = false;
                teacherDao.getConnection().rollback();
            }
            teacherDao.getConnection().commit();
            teacherDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean save(Teacher teacher) {
        boolean flag = true;
        try {
            teacherDao.getConnection().setAutoCommit(false);
            int result = teacherDao.save(teacher);
            if(result != 1){
                flag = false;
                teacherDao.getConnection().rollback();
            }
            teacherDao.getConnection().commit();
            teacherDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Course> getCourseListByTid(String id) {
        return teacherDao.getCourseListByTid(id);
    }

    @Override
    public boolean unBundCourse(String tid, String cid) {
        boolean flag = true;
        try {
            teacherDao.getConnection().setAutoCommit(false);
            int result = teacherDao.unBundCourse(tid,cid);
            if(result != 1){
                flag = false;
                teacherDao.getConnection().rollback();
            }
            teacherDao.getConnection().commit();
            teacherDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean bundCourse(TeacherCourseRelation teacherCourseRelation) {
        boolean flag = true;
        try {
            teacherDao.getConnection().setAutoCommit(false);
            int result = teacherDao.bundCourse(teacherCourseRelation);
            if(result != 1){
                flag = false;
                teacherDao.getConnection().rollback();
            }
            teacherDao.getConnection().commit();
            teacherDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Teacher> getTeacherListByCid(String cid) {
        return teacherDao.getTeacherListByCid(cid);
    }

    @Override
    public List<Teacher> getTeacherByIname(String instituteName) {
        return teacherDao.getTeacherByIname(instituteName);
    }
}