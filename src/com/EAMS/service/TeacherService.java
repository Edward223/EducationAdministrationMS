package com.EAMS.service;

import com.EAMS.domain.Course;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.TeacherCourseRelation;
import com.EAMS.vo.TeacherVo;

import java.util.List;
import java.util.Map;

public interface TeacherService {
    int getTeacherNum();

    List<TeacherVo> getTeacherVoList(Map<String, String> map);

    boolean deleteById(String id);

    boolean update(Teacher teacher);

    boolean save(Teacher teacher);

    List<Course> getCourseListByTid(String id);

    boolean unBundCourse(String tid, String cid);

    boolean bundCourse(TeacherCourseRelation teacherCourseRelation);

    List<Teacher> getTeacherListByCid(String toString);

    List<Teacher> getTeacherByIname(String instituteName);
}
