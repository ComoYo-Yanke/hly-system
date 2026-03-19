package com.hly.service;

import com.hly.dto.UserLoginDTO;
import com.hly.dto.UserPageDTO;
import com.hly.dto.UserUpdateDTO;
import com.hly.entity.User;
import com.hly.result.PageResult;


public interface UserService {
    User login(UserLoginDTO userLoginDTO);
    
    void register(UserLoginDTO userLoginDTO);
    
    void update(UserUpdateDTO userUpdateDTO);
    
    PageResult pageQuery(UserPageDTO userPageDTO);
}
