package com.EAMS.dao.impl;

import com.EAMS.dao.UserDao;
import com.EAMS.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public User login(String account, String password) {
        String sql = "select * from t_user where account=? and password=?";
        User user = null;
        ResultSet resultSet = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String sex = resultSet.getString("sex");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String institute = resultSet.getString("institute");
                String authority = resultSet.getString("authority");
                user = new User(id,account,password,name,sex,email,phone,institute,authority);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public int register(User user) {
        String sql = "insert into t_user values(?,?,?,?,?,?,?,?,?)";
        int result = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getId());
            preparedStatement.setString(2,user.getAccount());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4,user.getName());
            preparedStatement.setString(5,user.getSex());
            preparedStatement.setString(6,user.getEmail());
            preparedStatement.setString(7,user.getPhone());
            preparedStatement.setString(8,user.getInstitute());
            preparedStatement.setString(9,user.getAuthority());
            result = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public int getCountByAccount(String account) {
        String sql = "select count(*) from t_user where account=?";
        ResultSet resultSet = null;
        int result = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                result = resultSet.getInt(1);
            }
            System.out.println(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
