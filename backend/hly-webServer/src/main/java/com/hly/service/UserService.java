package com.hly.service;

import com.hly.dto.UserLoginDTO;
import com.hly.entity.User;


public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
