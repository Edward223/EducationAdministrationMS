package com.EAMS.util;

import java.sql.*;

public class JdbcUtil {

    static final String URL="jdbc:mysql://localhost:3306/eams?serverTimezone=UTC";
    static final String USERNAME="root";
    static final String PASSWORD="123456";
    static Connection con = null;

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("加载驱动成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getCon(){
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("连接数据库成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

}
