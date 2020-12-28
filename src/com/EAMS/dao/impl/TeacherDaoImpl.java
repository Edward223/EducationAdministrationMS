package com.EAMS.dao.impl;

import com.EAMS.dao.TeacherDao;
import com.EAMS.domain.Course;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.TeacherCourseRelation;
import com.EAMS.vo.TeacherVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeacherDaoImpl implements TeacherDao {
    Connection connection;

    public TeacherDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public int getTeacherNum() {
        String sql = "select count(*) from t_teacher";
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
    public List<TeacherVo> getTeacherVoList(Map<String, String> map) {
        String sql = "select t.id,t.name,t.sex,t.email,t.phone,i.name " +
                "from t_teacher t join t_institute i on t.instituteId=i.id " +
                "where t.name like ? and t.sex like ? and i.name like ?";
        ResultSet resultSet = null;
        List<TeacherVo> teacherList = new ArrayList<>();
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+map.get("name")+"%");
            preparedStatement.setString(2,"%"+map.get("sex")+"%");
            preparedStatement.setString(3,"%"+map.get("institute")+"%");
            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String tName = resultSet.getString(2);
                String sex = resultSet.getString(3);
                String email = resultSet.getString(4);
                String phone = resultSet.getString(5);
                String iName = resultSet.getString(6);
                TeacherVo teacherVo = new TeacherVo();
                teacherVo.setId(id);
                teacherVo.setName(tName);
                teacherVo.setSex(sex);
                teacherVo.setEmail(email);
                teacherVo.setPhone(phone);
                teacherVo.setInstituteName(iName);

                teacherList.add(teacherVo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teacherList;
    }

    @Override
    public int deleteById(String id) {
        String sql = "delete from t_teacher where id=?";
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
    public int update(Teacher teacher) {
        String sql = "update t_teacher set name=? , sex=? , email=? , phone=? , instituteId=? where id=?";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,teacher.getName());
            preparedStatement.setString(2,teacher.getSex());
            preparedStatement.setString(3,teacher.getEmail());
            preparedStatement.setString(4,teacher.getPhone());
            preparedStatement.setString(5,teacher.getInstituteId());
            preparedStatement.setString(6,teacher.getId());
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int save(Teacher teacher) {
        String sql = "insert into t_teacher values (?,?,?,?,?,?)";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,teacher.getId());
            preparedStatement.setString(2,teacher.getName());
            preparedStatement.setString(3,teacher.getSex());
            preparedStatement.setString(4,teacher.getEmail());
            preparedStatement.setString(5,teacher.getPhone());
            preparedStatement.setString(6,teacher.getInstituteId());
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public List<Course> getCourseListByTid(String id) {
        String sql = "SELECT * FROM t_course c join rela_teacher_course r on c.id = r.courseId where r.teacherId=?";
        ResultSet resultSet = null;
        List<Course> courseList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String cid = resultSet.getString(1);
                String name = resultSet.getString(2);
                String instituteId = resultSet.getString(3);
                String period = resultSet.getString(4);

                Course course = new Course();
                course.setId(cid);
                course.setName(name);
                course.setInstituteId(instituteId);
                course.setPeriod(period);
                courseList.add(course);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courseList;
    }

    @Override
    public int unBundCourse(String tid, String cid) {
        String sql = "delete from rela_teacher_course where teacherId=? and courseId=?";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,tid);
            preparedStatement.setString(2,cid);
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int bundCourse(TeacherCourseRelation teacherCourseRelation) {
        String sql = "insert into rela_teacher_course values (?,?,?)";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,teacherCourseRelation.getId());
            preparedStatement.setString(2,teacherCourseRelation.getTeacherId());
            preparedStatement.setString(3,teacherCourseRelation.getCourseId());
            System.out.println(preparedStatement);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public int getCourseNumByTid(String id) {
        String sql = "SELECT count(*) FROM rela_teacher_course where teacherId=?";
        ResultSet resultSet = null;
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            System.out.println(preparedStatement);
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
    public int unBundCourses(String id) {
        String sql = "delete from rela_teacher_course where teacherId=?";
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
    public List<Teacher> getTeacherListByCid(String cid) {
        String sql = "select t.id,t.name FROM rela_teacher_course tc " +
                "join t_teacher t on t.id=tc.teacherId " +
                "WHERE tc.courseId=?";
        ResultSet resultSet = null;
        List<Teacher> teacherList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,cid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);

                Teacher teacher = new Teacher();
                teacher.setId(id);
                teacher.setName(name);
                teacherList.add(teacher);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teacherList;
    }

    @Override
    public Teacher getTeacherWithMinWorkloadByCid(String cid) {
        String sql = "SELECT t.*\n" +
                "from rela_teacher_course tc \n" +
                "join t_course c on tc.courseId=c.id \n" +
                "join t_teacher t on t.id=tc.teacherId \n" +
                "left join t_timetale tt on tt.teacherId=t.id\n" +
                "WHERE c.id=? \n" +
                "GROUP BY t.name\n" +
                "ORDER BY count(tt.id)\n" +
                "limit 1";
        ResultSet resultSet = null;
        Teacher teacher = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,cid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);

                teacher = new Teacher();
                teacher.setId(id);
                teacher.setName(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teacher;
    }

    @Override
    public List<Teacher> getTeacherByIname(String instituteName) {
        String sql = "select t.id,t.name FROM t_institute i " +
                "join t_teacher t on i.id=t.instituteId " +
                "WHERE i.name=?";
        ResultSet resultSet = null;
        List<Teacher> teacherList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,instituteName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);

                Teacher teacher = new Teacher();
                teacher.setId(id);
                teacher.setName(name);
                teacherList.add(teacher);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teacherList;
    }
}
