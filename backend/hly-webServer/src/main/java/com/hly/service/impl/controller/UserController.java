package com.hly.service.impl.controller;

import com.hly.constant.JwtClaimsConstant;
import com.hly.dto.UserPageDTO;
import com.hly.dto.UserUpdateDTO;
import com.hly.properties.JwtProperties;
import com.hly.result.PageResult;
import com.hly.result.Result;
import com.hly.service.UIXService;
import com.hly.service.UserService;
import com.hly.utils.JwtUtil;
import com.hly.dto.UserLoginDTO;
import com.hly.entity.User;
import com.hly.utils.RedisUtil;
import com.hly.utils.ThreadLocalUtil;
import com.hly.vo.UserLoginVO;
import com.hly.vo.UserQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisUtil redisUtil;
    
    @PostMapping("/login")
    @ApiModelProperty("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ID, user.getId());
        log.info("登录用户：{}",user.getId());
        
        String token = JwtUtil.createJWT(3, claims);
        
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .token(token)
                .build();
        
        // 将当前用户id以及 token 存入redis
        ThreadLocalUtil.setCurrentIdS(userLoginVO.getId());
        redisUtil.setToken(userLoginVO.getId(), userLoginVO.getToken());
        return Result.success(userLoginVO);
    }
    
    @PostMapping("/register")
    @ApiModelProperty("用户注册")
    public Result register(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户注册：{}", userLoginDTO);
        userService.register(userLoginDTO);
        
        return Result.success();
    }
    
    
    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    @ApiModelProperty("登出")
    public Result<String> logout() {
        log.info("登出：{}", ThreadLocalUtil.getCurrentIdS());
        userService.logout(ThreadLocalUtil.getCurrentIdS());
        return Result.success();
    }
    
    @GetMapping("/test")
    public Result<String> test(){
        System.out.println(ThreadLocalUtil.getCurrentIdS());
        
        return Result.success("test");
    }
    
    @PutMapping
    @ApiModelProperty("更新用户基本信息")
    public Result update(@RequestBody UserUpdateDTO userUpdateDTO){
        log.info("更新用户：{} , {}", userUpdateDTO.getName());
        userService.update(userUpdateDTO);
        return Result.success();
    }
    
    @GetMapping("/page")
    @ApiModelProperty("普通用户查询用户列表")
    public Result<PageResult> page(@RequestBody UserPageDTO userPageDTO){
        log.info("分页查询 {}", userPageDTO);
        PageResult pageResult = userService.pageQuery(userPageDTO);
        return Result.success(pageResult);
    }
    
    @PostMapping("/close")
    @ApiModelProperty("注销账户")
    public Result signOff(){
        Integer id = ThreadLocalUtil.getCurrentIdS();
        log.info("注销用户：{}", id);
        userService.signOff(id);
        return Result.success();
    }
    
    @GetMapping
    @ApiModelProperty("根据id获取用户基本信息")
    public Result<UserQueryVO> queryUserById(){
        Integer id = ThreadLocalUtil.getCurrentIdS();
        log.info("查询员工基本信息：{}", id);
        
        UserQueryVO userQueryVO = userService.queryById(id);
        if(userQueryVO == null)return Result.error("不可夺舍");
        
        return Result.success(userQueryVO);
    }
    
    @GetMapping("/fan")
    @ApiModelProperty("查询用户粉丝数量")
    public Result<Integer> getFansCount(){
        log.info("查询用户粉丝数");
        Integer fansCount = userService.getCurrentUserFansCount();
        return Result.success(fansCount);
    }
    
    @GetMapping("/follow")
    @ApiModelProperty("查询用户粉丝数量")
    public Result<Integer> pageForFansCount(){
        log.info("查询用户关注数");
        Integer followCount = userService.getCurrentUserFollowCount();
        return Result.success(followCount);
    }
}
