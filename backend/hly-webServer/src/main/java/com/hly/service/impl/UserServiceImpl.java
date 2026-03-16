package com.hly.service.impl;

import com.hly.constant.InfoConstant;
import com.hly.exception.AccountLockedException;
import com.hly.exception.AccountNotFoundException;
import com.hly.exception.PasswordErrorException;
import com.hly.dto.UserLoginDTO;
import com.hly.entity.User;
import com.hly.mapper.UserMapper;
import com.hly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        
        User user = userMapper.getByUsername(username);
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(InfoConstant.ACCOUNT_NOT_FOUND);
        }
        //密码比对
        // 对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(InfoConstant.PASSWORD_ERROR);
        }
        
        if (user.getStatus() == InfoConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(InfoConstant.ACCOUNT_LOCKED);
        }
        return user;
    }
}
