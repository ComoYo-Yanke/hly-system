package com.hly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hly.constant.DataConstant;
import com.hly.constant.InfoConstant;
import com.hly.dto.UserPageDTO;
import com.hly.dto.UserUpdateDTO;
import com.hly.exception.AccountLockedException;
import com.hly.exception.AccountNotFoundException;
import com.hly.exception.PasswordErrorException;
import com.hly.dto.UserLoginDTO;
import com.hly.entity.User;
import com.hly.mapper.UserMapper;
import com.hly.result.PageResult;
import com.hly.service.UserService;
import com.hly.utils.RedisUtil;
import com.hly.utils.ThreadLocalUtil;
import com.hly.vo.UserQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    
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
        user.setRole(InfoConstant.ROLE_NORMAL); // 初始角色 默认为普通用户
        user.setGender(InfoConstant.GENDER_MAN); // 初始性别 默认为男
        user.setBio(null);
        user.setEmail(null);
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
    
    @Override
    public PageResult pageQuery(UserPageDTO userPageDTO){
        // TODO 后续新增其他排序方式：如按照被关注总数，当前用户关注置顶
        if(userPageDTO.getPage() == null) userPageDTO.setPage(DataConstant.DEFAULT_PAGE);
        if(userPageDTO.getPageSize() == null) userPageDTO.setPageSize(DataConstant.DEFAULT_PAGE_SIZE);
        log.info("用户分页查询：{}", userPageDTO);
        PageHelper.startPage(userPageDTO.getPage(), userPageDTO.getPageSize());
        Page<User> page = userMapper.pageQuery(userPageDTO);
        long total = page.getTotal();
        List<User> records = page.getResult();
        
        return new PageResult(total, records);
    }
    
    @Override
    public void logout(Integer currentIdS){
        redisUtil.deleteToken(currentIdS);
    }
    
    @Override
    public void signOff(Integer id){
        userMapper.deleteById(id);
        redisUtil.deleteToken(id);
    }
    
    @Override
    public UserQueryVO queryById(Integer id){
        UserQueryVO userQueryVO = new UserQueryVO();
        if(ThreadLocalUtil.getCurrentIdS() != id) {
            log.error("不可查询其他用户的个人信息");
            return null;
        }
        User user = userMapper.query(id);
        BeanUtils.copyProperties(user, userQueryVO);
        
        return userQueryVO;
    }
}
