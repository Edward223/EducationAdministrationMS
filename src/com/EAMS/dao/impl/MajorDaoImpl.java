package com.EAMS.dao.impl;

import com.EAMS.dao.MajorDao;
import com.EAMS.domain.Major;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MajorDaoImpl implements MajorDao {
    Connection connection;

    public MajorDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<Major> getMajorListByIid(String iid) {
        String sql = "SELECT * FROM t_major where instituteId=?";
        ResultSet resultSet = null;
        List<Major> majorList = new ArrayList<>();
        int num = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,iid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);

                Major major = new Major();
                major.setId(id);
                major.setName(name);
                major.setInstituteId(iid);
                majorList.add(major);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return majorList;
    }

    @Override
    public String getMajorIdByName(String inMajor) {
        String sql = "SELECT id FROM t_major where name=?";
        ResultSet resultSet = null;
        String id = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,inMajor);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override
    public List<Major> getMajorListByIname(String iName) {
        String sql = "SELECT m.id,m.name,m.instituteId from t_major m join t_institute i on i.id=m.instituteId WHERE i.name=?";
        ResultSet resultSet = null;
        List<Major> majorList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,iName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String instituteId = resultSet.getString(3);

                Major major = new Major();
                major.setId(id);
                major.setName(name);
                major.setInstituteId(instituteId);
                majorList.add(major);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return majorList;
    }
}
