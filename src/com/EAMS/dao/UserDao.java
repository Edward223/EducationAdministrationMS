package com.EAMS.dao;

import com.EAMS.domain.User;

public interface UserDao {
    User login(String account, String password);

    int register(User user);

    int getCountByAccount(String account);
}
