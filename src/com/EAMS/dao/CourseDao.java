package com.EAMS.dao;

import com.EAMS.domain.Course;
import com.EAMS.domain.CourseMajorRelation;
import com.EAMS.vo.CoursePlanVo;
import com.EAMS.vo.CourseVo;

import java.util.List;
import java.util.Map;

public interface CourseDao {
    int getCourseNum();

    List<Course> getCourseListByIid(String instituteId);

    List<CourseVo> getCourseVoList(Map<String, String> map);

    Integer getMajorNumById(String id);

    Integer getTeacherNumById(String id);

    int deleteById(String id);

    int unBundTeacher(String id);

    int unBundMajor(String id);

    int save(Course course);

    List<CoursePlanVo> getPlanListById(String cid);

    int deletePlanById(String id);

    int savePlan(CourseMajorRelation courseMajorRelation);

    List<Course> getCoursePlanListByClass(String majorName, String grade, String semester);

    Course getCourseById(String courseId);
}
