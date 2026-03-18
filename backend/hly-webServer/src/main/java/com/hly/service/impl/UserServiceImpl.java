package com.hly.service.impl;

import com.hly.constant.InfoConstant;
import com.hly.dto.UserUpdateDTO;
import com.hly.exception.AccountLockedException;
import com.hly.exception.AccountNotFoundException;
import com.hly.exception.PasswordErrorException;
import com.hly.dto.UserLoginDTO;
import com.hly.entity.User;
import com.hly.mapper.UserMapper;
import com.hly.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
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
    
    @Override
    public void register(UserLoginDTO userLoginDTO) {
        User user = new User();
        BeanUtils.copyProperties(userLoginDTO, user);
        
        // 初始化数据
        user.setStatus(InfoConstant.ENABLE);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setName(userLoginDTO.getUsername());  // 初始姓名与账号（username）相同
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setRole(0);
        user.setGender(1);
        // TODO 后续添加默认初始头像
        user.setAvatar(null);
        
        userMapper.insert(user);
        
    }
    
    @Override
    public void update(UserUpdateDTO userUpdateDTO){
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setUpdatedTime(LocalDateTime.now());
        
        if(!(user.getPassword() == null || user.getPassword() == "")){
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        log.info("更新用户基本信息：{}",user);
        userMapper.updateById(user);
    }
}
