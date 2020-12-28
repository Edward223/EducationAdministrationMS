package com.EAMS.dao;

import com.EAMS.domain.Course;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.TeacherCourseRelation;
import com.EAMS.vo.TeacherVo;

import java.util.List;
import java.util.Map;

public interface TeacherDao {
    int getTeacherNum();

    List<TeacherVo> getTeacherVoList(Map<String, String> map);

    int deleteById(String id);

    int update(Teacher teacher);

    int save(Teacher teacher);

    List<Course> getCourseListByTid(String id);

    int unBundCourse(String tid, String cid);

    int bundCourse(TeacherCourseRelation teacherCourseRelation);

    int getCourseNumByTid(String id);

    int unBundCourses(String id);

    List<Teacher> getTeacherListByCid(String cName);

    Teacher getTeacherWithMinWorkloadByCid(String cid);

    List<Teacher> getTeacherByIname(String instituteName);
}
