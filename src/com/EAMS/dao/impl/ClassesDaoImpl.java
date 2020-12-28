package com.EAMS.dao.impl;

import com.EAMS.dao.ClassesDao;
import com.EAMS.domain.Classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassesDaoImpl implements ClassesDao {
    Connection connection;

    public ClassesDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public int getClassNum() {
        String sql = "select count(*) from t_class";
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
    public List<Classes> getClassListByCondition(String majorName, String grade) {
        String sql = "SELECT c.id, classNo, studentNum from t_class c join t_major m on c.majorId=m.id WHERE m.name=? and c.year=?";
        ResultSet resultSet = null;
        List<Classes> classesList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,majorName);
            preparedStatement.setString(2,grade);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String classNo = resultSet.getString(2);
                String studentNum = resultSet.getString(3);

                Classes classes = new Classes();
                classes.setId(id);
                classes.setYear(grade);
                classes.setClassNo(classNo);
                classes.setStudentNum(studentNum);
                classesList.add(classes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classesList;
    }

    @Override
    public Classes getClassByCondition(String major, String year, String classNo) {
        String sql = "SELECT c.id, studentNum from t_class c join t_major m on c.majorId=m.id WHERE m.name=? and c.year=? and c.classNo=?";
        ResultSet resultSet = null;
        Classes classes = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,major);
            preparedStatement.setString(2,year);
            preparedStatement.setString(3,classNo);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String studentNum = resultSet.getString(2);

                classes = new Classes();
                classes.setId(id);
                classes.setYear(year);
                classes.setStudentNum(studentNum);
                classes.setClassNo(classNo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classes;
    }

    @Override
    public Classes getClassById(String classId) {
        String sql = "select id, year, instituteId, majorId, classNo, studentNum from t_class where id=?";
        ResultSet resultSet = null;
        Classes classes = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,classId);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String year = resultSet.getString(2);
                String instituteId = resultSet.getString(3);
                String majorId = resultSet.getString(4);
                String classNo = resultSet.getString(5);
                String studentNum = resultSet.getString(6);

                classes = new Classes();
                classes.setId(id);
                classes.setYear(year);
                classes.setInstituteId(instituteId);
                classes.setMajorId(majorId);
                classes.setClassNo(classNo);
                classes.setStudentNum(studentNum);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classes;
    }
}
