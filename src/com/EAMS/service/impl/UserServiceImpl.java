package com.EAMS.service.impl;

import com.EAMS.dao.impl.UserDaoImpl;
import com.EAMS.domain.User;
import com.EAMS.exception.LoginException;
import com.EAMS.service.UserService;
import com.EAMS.util.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    Connection connection = JdbcUtil.getCon();
    UserDaoImpl userDao = new UserDaoImpl(connection);

    @Override
    public User login(String account ,String password) throws LoginException {
        User user = userDao.login(account,password);

        if(user==null){
            throw new LoginException("账号密码错误！请重新登录！");
        }

        return user;
    }

    @Override
    public boolean register(User user) {
        boolean flag = false;
        //判断用户所注册的用户名是否已经存在，若存在则无法注册
        int duplicationAccountCount = userDao.getCountByAccount(user.getAccount());
        if(duplicationAccountCount == 0){
            try {
                userDao.getConnection().setAutoCommit(false);
                int result = userDao.register(user);
                if(result == 1){
                    flag = true;
                    userDao.getConnection().commit();
                    userDao.getConnection().setAutoCommit(true);
                }else {
                    userDao.getConnection().rollback();
                }
            }catch (SQLException throwables){
                throwables.printStackTrace();
            }
        }
        return flag;
    }
}
