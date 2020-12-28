package com.EAMS.service.impl;

import com.EAMS.dao.impl.CourseDaoImpl;
import com.EAMS.domain.Course;
import com.EAMS.domain.CourseMajorRelation;
import com.EAMS.service.CourseService;
import com.EAMS.util.JdbcUtil;
import com.EAMS.vo.CoursePlanVo;
import com.EAMS.vo.CourseVo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CourseServiceImpl implements CourseService {
    Connection connection = JdbcUtil.getCon();
    CourseDaoImpl courseDao = new CourseDaoImpl(connection);

    @Override
    public int getCourseNum() {
        return courseDao.getCourseNum();
    }

    @Override
    public List<Course> getCourseListByIid(String instituteId) {
        return courseDao.getCourseListByIid(instituteId);
    }

    @Override
    public List<CourseVo> getCourseVoList(Map<String, String> map) {
        List<CourseVo> courseVoList = courseDao.getCourseVoList(map);

        courseVoList.forEach(c -> {
            c.setMajorNum(courseDao.getMajorNumById(c.getId()));
            c.setTeacherNum(courseDao.getTeacherNumById(c.getId()));
        });

        return courseVoList;
    }

    @Override
    public boolean deleteById(String id) {
        boolean flag = true;
        try {
            courseDao.getConnection().setAutoCommit(false);
            int result1 = courseDao.deleteById(id);
            if(result1 != 1){
                flag = false;
                courseDao.getConnection().rollback();
            }
            int teacherNum = courseDao.getTeacherNumById(id);
            int result2 = courseDao.unBundTeacher(id);
            if(result2 != teacherNum){
                flag = false;
                courseDao.getConnection().rollback();
            }
            int majorNum = courseDao.getMajorNumById(id);
            int result3 = courseDao.unBundMajor(id);
            if(result3 != majorNum){
                flag = false;
                courseDao.getConnection().rollback();
            }
            courseDao.getConnection().commit();
            courseDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean save(Course course) {
        boolean flag = true;
        try {
            courseDao.getConnection().setAutoCommit(false);
            int result = courseDao.save(course);
            if(result != 1){
                flag = false;
                courseDao.getConnection().rollback();
            }
            courseDao.getConnection().commit();
            courseDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<CoursePlanVo> getPlanById(String id) {
        return courseDao.getPlanListById(id);
    }

    @Override
    public boolean deletePlanById(String id) {
        boolean flag = true;
        try {
            courseDao.getConnection().setAutoCommit(false);
            int result = courseDao.deletePlanById(id);
            if(result != 1){
                flag = false;
                courseDao.getConnection().rollback();
            }
            courseDao.getConnection().commit();
            courseDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean savePlan(CourseMajorRelation courseMajorRelation) {
        boolean flag = true;
        try {
            courseDao.getConnection().setAutoCommit(false);
            int result = courseDao.savePlan(courseMajorRelation);
            if(result != 1){
                flag = false;
                courseDao.getConnection().rollback();
            }
            courseDao.getConnection().commit();
            courseDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Course> getCoursePlanListByClass(String majorName, String grade, String semester) {
        return courseDao.getCoursePlanListByClass(majorName, grade, semester);
    }
}
