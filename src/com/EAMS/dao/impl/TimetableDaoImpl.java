package com.EAMS.dao.impl;

import com.EAMS.dao.TimetableDao;
import com.EAMS.domain.Timetable;
import com.EAMS.vo.TimetableVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableDaoImpl implements TimetableDao {
    Connection connection;

    public TimetableDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<TimetableVo> getTimetableVoList(Map<String, String> map) {
        String sql = "SELECT tt.id,co.name,te.name,m.name,cl.year,cl.classNo,clr.area,clr.floor,clr.roomNo,tt.weeks,tt.timeslotId from t_timetale tt\n" +
                "join t_teacher te on te.id=tt.teacherId\n" +
                "join t_class cl on cl.id=tt.classId\n" +
                "join t_course co on co.id=tt.courseId\n" +
                "join t_major m on m.id=cl.majorId\n" +
                "join t_classroom clr on clr.id=tt.classroomId\n" +
                "WHERE te.name like ? and co.name like ? and clr.id like ? and tt.weeks like ? and cl.instituteId like ? and cl.majorId like ? and cl.year like ? and cl.classNo like ?\n";
        ResultSet resultSet = null;
        List<TimetableVo> timetableList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+map.get("teacher")+"%");
            preparedStatement.setString(2,"%"+map.get("course")+"%");
            preparedStatement.setString(3,"%"+map.get("classroom")+"%");
            preparedStatement.setString(4,"%"+map.get("week")+"%");
            preparedStatement.setString(5,"%"+map.get("institute")+"%");
            preparedStatement.setString(6,"%"+map.get("major")+"%");
            preparedStatement.setString(7,"%"+map.get("year")+"%");
            preparedStatement.setString(8,"%"+map.get("classNo")+"%");
            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String courseName = resultSet.getString(2);
                String teacherName = resultSet.getString(3);
                String majorName = resultSet.getString(4);
                String year = resultSet.getString(5);
                String classNo = resultSet.getString(6);
                String area = resultSet.getString(7);
                String floor = resultSet.getString(8);
                String roomNo = resultSet.getString(9);
                String weeks = resultSet.getString(10);
                String timeslot = resultSet.getString(11);

                TimetableVo timetableVo = new TimetableVo();
                timetableVo.setId(id);
                timetableVo.setCourseName(courseName);
                timetableVo.setTeacherName(teacherName);
                timetableVo.setMajorName(majorName);
                timetableVo.setClassName(year+"("+classNo+")");
                timetableVo.setClassroomName(area+"-"+floor+roomNo);
                timetableVo.setWeek(weeks);
                timetableVo.setTimeslot(timeslot);

                timetableList.add(timetableVo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return timetableList;
    }

    @Override
    public int deleteById(String id) {
        String sql = "delete from t_timetale where id=?";
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
    public int getTimetableNum() {
        String sql = "select count(*) from t_timetale";
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
    public int init() {
        String sql = "delete from t_timetale";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public String getWeeksByRoomAndTimeslot(String classroomId, String timeslot) {
        String sql = "select weeks from t_timetale where classroomId=? and timeslotId like ?";
        ResultSet resultSet = null;
        String weeks = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,classroomId);
            preparedStatement.setString(2,"%"+timeslot+"%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                weeks = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        weeks = weeks.replace("(","");
        weeks = weeks.replace(")","");
        return weeks;
    }

    @Override
    public String getWeeksByTeacherAndTimeslot(String teacherId, String timeslot) {
        String sql = "select weeks from t_timetale where teacherId=? and timeslotId like ?";
        ResultSet resultSet = null;
        String weeks = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,teacherId);
            preparedStatement.setString(2,"%"+timeslot+"%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                weeks = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        weeks = weeks.replace("(","");
        weeks = weeks.replace(")","");
        return weeks;
    }

    @Override
    public int save(Timetable timetable) {
        String sql = "insert into t_timetale values (?,?,?,?,?,?,?)";
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,timetable.getId());
            preparedStatement.setString(2,timetable.getClassId());
            preparedStatement.setString(3,timetable.getCourseId());
            preparedStatement.setString(4,timetable.getClassroomId());
            preparedStatement.setString(5,timetable.getTeacherId());
            preparedStatement.setString(6,timetable.getWeeks());
            preparedStatement.setString(7,timetable.getTimeslotId());
            num = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    @Override
    public String getWeeksByClassesAndTimeslot(String classId, String timeslot) {
        String sql = "select weeks from t_timetale where classId=? and timeslotId like ?";
        ResultSet resultSet = null;
        String weeks = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,classId);
            preparedStatement.setString(2,"%"+timeslot+"%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                weeks = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        weeks = weeks.replace("(","");
        weeks = weeks.replace(")","");
        return weeks;
    }

    @Override
    public int getTimetableByClassAndCourse(String classId, String courseId) {
        String sql = "select count(*) from t_timetale where classId=? and courseId=?";
        ResultSet resultSet = null;
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,classId);
            preparedStatement.setString(2,courseId);
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
    public Map<String, Integer> getWorkTime(String week) {
        String sql = "select timeslotId from t_timetale where weeks like ?";
        ResultSet resultSet = null;
        HashMap<String,Integer> map = new HashMap<>();
        Integer mon = 0;
        Integer tue = 0;
        Integer wed = 0;
        Integer thu = 0;
        Integer fri = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if(week.isEmpty()){
                preparedStatement.setString(1,"%"+week+"%");
            }else {
                preparedStatement.setString(1,"%("+week+")%");
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String timeslot = resultSet.getString(1);
                String[] timeslots = timeslot.split(",");
                for (int i = 0; i < timeslots.length; i++) {
                    switch (timeslots[i].charAt(0)){
                        case '1':mon++;break;
                        case '2':tue++;break;
                        case '3':wed++;break;
                        case '4':thu++;break;
                        case '5':fri++;break;
                        default:break;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        map.put("周一",mon);
        map.put("周二",tue);
        map.put("周三",wed);
        map.put("周四",thu);
        map.put("周五",fri);
        return map;
    }

    @Override
    public Map<String, Integer> getWorkTimeByTeacher(String week, String tName) {
        String sql = "select tt.timeslotId from t_timetale tt join t_teacher t on t.id=tt.teacherId " +
                "where tt.weeks like ? and t.name=?";
        ResultSet resultSet = null;
        HashMap<String,Integer> map = new HashMap<>();
        Integer mon = 0;
        Integer tue = 0;
        Integer wed = 0;
        Integer thu = 0;
        Integer fri = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if(week.isEmpty()){
                preparedStatement.setString(1,"%"+week+"%");
            }else {
                preparedStatement.setString(1,"%("+week+")%");
            }
            preparedStatement.setString(2,tName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String timeslot = resultSet.getString(1);
                String[] timeslots = timeslot.split(",");
                for (int i = 0; i < timeslots.length; i++) {
                    switch (timeslots[i].charAt(0)){
                        case '1':mon++;break;
                        case '2':tue++;break;
                        case '3':wed++;break;
                        case '4':thu++;break;
                        case '5':fri++;break;
                        default:break;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        map.put("周一",mon);
        map.put("周二",tue);
        map.put("周三",wed);
        map.put("周四",thu);
        map.put("周五",fri);
        return map;
    }

}
