package com.EAMS.dao.impl;

import com.EAMS.dao.CourseDao;
import com.EAMS.domain.Course;
import com.EAMS.domain.CourseMajorRelation;
import com.EAMS.vo.CoursePlanVo;
import com.EAMS.vo.CourseVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseDaoImpl implements CourseDao {
    Connection connection;

    public CourseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public int getCourseNum() {
        String sql = "select count(*) from t_course";
        ResultSet resultSet = null;
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                num = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public List<Course> getCourseListByIid(String instituteId) {
        String sql = "SELECT * FROM t_course where instituteId=?";
        ResultSet resultSet = null;
        List<Course> courseList = new ArrayList<>();
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,instituteId);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String institute = resultSet.getString(3);
                String period = resultSet.getString(4);

                Course course = new Course();
                course.setId(id);
                course.setName(name);
                course.setInstituteId(institute);
                course.setPeriod(period);
                courseList.add(course);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courseList;
    }

    @Override
    public List<CourseVo> getCourseVoList(Map<String, String> map) {
        String sql = "SELECT c.id,c.name,i.name,c.period " +
                "FROM t_course c join t_institute i on i.id=c.instituteId "+
                "WHERE c.name like ? and i.name like ?";
        ResultSet resultSet = null;
        List<CourseVo> courseList = new ArrayList<>();
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+map.get("name")+"%");
            preparedStatement.setString(2,"%"+map.get("institute")+"%");
            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String tName = resultSet.getString(2);
                String iName = resultSet.getString(3);
                String period = resultSet.getString(4);
                CourseVo courseVo = new CourseVo();
                courseVo.setId(id);
                courseVo.setName(tName);
                courseVo.setInstituteName(iName);
                courseVo.setPeriod(period);

                courseList.add(courseVo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courseList;
    }

    @Override
    public Integer getMajorNumById(String id) {
        String sql = "SELECT count(*) FROM t_course c join rela_course_major cm on cm.courseId=c.id WHERE c.id=?";
        ResultSet resultSet = null;
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                num = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public Integer getTeacherNumById(String id) {
        String sql = "SELECT count(*) FROM t_course c join rela_teacher_course tc on tc.courseId=c.id WHERE c.id=?";
        ResultSet resultSet = null;
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                num = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int deleteById(String id) {
        String sql = "delete from t_course where id=?";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int unBundTeacher(String id) {
        String sql = "delete from rela_teacher_course where courseId=?";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int unBundMajor(String id) {
        String sql = "delete from rela_course_major where courseId=?";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int save(Course course) {
        String sql = "insert into t_course values (?,?,?,?)";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,course.getId());
            preparedStatement.setString(2,course.getName());
            preparedStatement.setString(3,course.getInstituteId());
            preparedStatement.setString(4,course.getPeriod());
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public List<CoursePlanVo> getPlanListById(String cid) {
        String sql = "SELECT cm.id,m.name,cm.grade,cm.semester " +
                "FROM rela_course_major cm join t_major m on m.id=cm.majorId " +
                "WHERE cm.courseId=?";
        ResultSet resultSet = null;
        List<CoursePlanVo> coursePlanList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,cid);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String rid = resultSet.getString(1);
                String majorName = resultSet.getString(2);
                String grade = resultSet.getString(3);
                String semester = resultSet.getString(4);

                CoursePlanVo coursePlanVo = new CoursePlanVo();
                coursePlanVo.setId(rid);
                coursePlanVo.setCourseId(cid);
                coursePlanVo.setMajorName(majorName);
                coursePlanVo.setGrade(grade);
                coursePlanVo.setSemester(semester);

                coursePlanList.add(coursePlanVo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return coursePlanList;
    }

    @Override
    public int deletePlanById(String id) {
        String sql = "delete from rela_course_major where id=?";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int savePlan(CourseMajorRelation courseMajorRelation) {
        String sql = "insert into rela_course_major values (?,?,?,?,?)";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,courseMajorRelation.getId());
            preparedStatement.setString(2,courseMajorRelation.getCourseId());
            preparedStatement.setString(3,courseMajorRelation.getMajorId());
            preparedStatement.setString(4,courseMajorRelation.getGrade());
            preparedStatement.setString(5,courseMajorRelation.getSemester());
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public List<Course> getCoursePlanListByClass(String majorName, String grade, String semester) {
        String sql = "select c.id,c.name,c.period FROM rela_course_major cm " +
                "join t_major m on m.id=cm.majorId " +
                "join t_course c on c.id=cm.courseId " +
                "WHERE m.name like ? and cm.grade=? and cm.semester=?";
        ResultSet resultSet = null;
        List<Course> coursePlanList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+majorName+"%");
            preparedStatement.setString(2,grade);
            preparedStatement.setString(3,semester);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String period = resultSet.getString(3);

                Course course = new Course();
                course.setId(id);
                course.setName(name);
                course.setPeriod(period);

                coursePlanList.add(course);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return coursePlanList;
    }

    @Override
    public Course getCourseById(String courseId) {
        String sql = "select id, name, instituteId, period from t_course where id=?";
        ResultSet resultSet = null;
        Course course = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,courseId);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String instituteId = resultSet.getString(3);
                String period = resultSet.getString(4);

                course = new Course();
                course.setId(id);
                course.setName(name);
                course.setInstituteId(instituteId);
                course.setPeriod(period);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return course;
    }


}
