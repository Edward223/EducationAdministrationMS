package com.EAMS.dao.impl;

import com.EAMS.dao.InstituteDao;
import com.EAMS.domain.Institute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstituteDaoImpl implements InstituteDao {
    Connection connection;

    public InstituteDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<Institute> getInstituteList() {
        String sql = "select * from t_institute";
        ResultSet resultSet = null;
        Institute institute = null;
        List<Institute> instituteList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                institute = new Institute();
                institute.setId(id);
                institute.setName(name);
                instituteList.add(institute);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return instituteList;
    }

    @Override
    public String getInstituteIdByName(String name) {
        String sql = "SELECT id FROM t_institute where name=?";
        ResultSet resultSet = null;
        String id = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }
}
