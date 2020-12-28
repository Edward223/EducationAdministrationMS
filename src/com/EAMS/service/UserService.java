package com.EAMS.service;

import com.EAMS.domain.User;
import com.EAMS.exception.LoginException;

public interface UserService {
    User login(String account ,String password) throws LoginException;

    boolean register(User user);
}
