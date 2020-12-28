package com.EAMS.dao.impl;

import com.EAMS.dao.TimeslotDao;
import com.EAMS.domain.Timeslot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimeslotDaoImpl implements TimeslotDao {
    Connection connection;

    public TimeslotDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Timeslot getTimeslotById(String id) {
        String sql = "select day, time from t_timeslot where id=?";
        ResultSet resultSet = null;
        Timeslot timeslot = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String day = resultSet.getString(1);
                String time = resultSet.getString(2);
                timeslot = new Timeslot();
                timeslot.setId(id);
                timeslot.setDay(day);
                timeslot.setTime(time);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return timeslot;
    }

    @Override
    public Timeslot getTimeslotByCondition(String day, String time) {
        String sql = "select id from t_timeslot where day=? and time=?";
        ResultSet resultSet = null;
        Timeslot timeslot = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,day);
            preparedStatement.setString(2,time);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                timeslot = new Timeslot();
                timeslot.setId(id);
                timeslot.setDay(day);
                timeslot.setTime(time);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return timeslot;
    }

    @Override
    public List<Timeslot> getTimeslotList() {
        String sql = "select id,day,time from t_timeslot";
        ResultSet resultSet = null;
        List<Timeslot> timeslotList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String day = resultSet.getString(2);
                String time = resultSet.getString(3);

                Timeslot timeslot = new Timeslot();
                timeslot.setId(id);
                timeslot.setDay(day);
                timeslot.setTime(time);

                timeslotList.add(timeslot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return timeslotList;
    }
}
