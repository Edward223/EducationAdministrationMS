package com.EAMS.dao.impl;

import com.EAMS.dao.ClassroomDao;
import com.EAMS.domain.Classroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassroomDaoImpl implements ClassroomDao {
    Connection connection;

    public ClassroomDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public int getClassNum() {
        String sql = "select count(*) from t_classroom";
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
    public List<Classroom> getClassroomList() {
        String sql = "select area, floor, roomNo from t_classroom";
        ResultSet resultSet = null;
        Classroom classroom = null;
        List<Classroom> classroomList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String area = resultSet.getString("area");
                String floor = resultSet.getString("floor");
                String roomNo = resultSet.getString("roomNo");
                classroom = new Classroom();
                classroom.setArea(area);
                classroom.setFloor(floor);
                classroom.setRoomNo(roomNo);

                classroomList.add(classroom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classroomList;
    }

    @Override
    public Classroom getClassroomByCondition(Map<String, String> map) {
        String sql = "select id, holdNum from t_classroom where area=? and floor=? and roomNo=?";
        ResultSet resultSet = null;
        Classroom classroom = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,map.get("area"));
            preparedStatement.setString(2,map.get("floor"));
            preparedStatement.setString(3,map.get("roomNo"));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String holdNum = resultSet.getString(2);

                classroom = new Classroom();
                classroom.setId(id);
                classroom.setArea(map.get("area"));
                classroom.setFloor(map.get("floor"));
                classroom.setRoomNo(map.get("roomNo"));
                classroom.setHoldNum(holdNum);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classroom;
    }

    @Override
    public Classroom getClassroomById(String classroomId) {
        String sql = "select id, area, floor, roomNo, holdNum from t_classroom where id=?";
        ResultSet resultSet = null;
        Classroom classroom = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,classroomId);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String area = resultSet.getString(2);
                String floor = resultSet.getString(3);
                String roomNo = resultSet.getString(4);
                String holdNum = resultSet.getString(5);

                classroom = new Classroom();
                classroom.setId(id);
                classroom.setArea(area);
                classroom.setFloor(floor);
                classroom.setRoomNo(roomNo);
                classroom.setHoldNum(holdNum);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classroom;
    }

    @Override
    public List<Classroom> getClassroomListByHoldNum(String studentNum) {
        String sql = "select id,area, floor, roomNo, holdNum from t_classroom where holdNum > ?";
        ResultSet resultSet = null;
        List<Classroom> classroomList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,studentNum);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String area = resultSet.getString(2);
                String floor = resultSet.getString(3);
                String roomNo = resultSet.getString(4);
                String holdNum = resultSet.getString(5);

                Classroom classroom = new Classroom();
                classroom.setId(id);
                classroom.setArea(area);
                classroom.setFloor(floor);
                classroom.setRoomNo(roomNo);
                classroom.setHoldNum(holdNum);

                classroomList.add(classroom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classroomList;
    }
}
