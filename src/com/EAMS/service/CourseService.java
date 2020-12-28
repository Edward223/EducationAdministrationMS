package com.EAMS.service;

import com.EAMS.domain.Course;
import com.EAMS.domain.CourseMajorRelation;
import com.EAMS.vo.CoursePlanVo;
import com.EAMS.vo.CourseVo;

import java.util.List;
import java.util.Map;

public interface CourseService {

    int getCourseNum();

    List<Course> getCourseListByIid(String instituteId);

    List<CourseVo> getCourseVoList(Map<String, String> map);

    boolean deleteById(String id);

    boolean save(Course course);

    List<CoursePlanVo> getPlanById(String id);

    boolean deletePlanById(String id);

    boolean savePlan(CourseMajorRelation courseMajorRelation);

    List<Course> getCoursePlanListByClass(String majorName, String s, String s1);
}
