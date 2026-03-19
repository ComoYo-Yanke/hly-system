package com.hly.controller;

import com.hly.constant.JwtClaimsConstant;
import com.hly.dto.UserPageDTO;
import com.hly.dto.UserUpdateDTO;
import com.hly.properties.JwtProperties;
import com.hly.result.PageResult;
import com.hly.result.Result;
import com.hly.service.UserService;
import com.hly.utils.JwtUtil;
import com.hly.dto.UserLoginDTO;
import com.hly.entity.User;
import com.hly.vo.UserLoginVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    
    @PostMapping("/login")
    @ApiModelProperty("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ID, user.getId());
        
        String token = JwtUtil.createJWT(
                3,
                claims);
        
        
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .token(token)
                .build();
        
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
        return Result.success();
    }
    
    @GetMapping("/test")
    public Result<String> test(){
        return Result.success("test");
    }
    
    @PutMapping
    @ApiModelProperty("更新用户基本信息")
    public Result update(@RequestBody UserUpdateDTO userUpdateDTO){
        log.info("更新用户：{} , {}", userUpdateDTO.getId(), userUpdateDTO.getName());
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
}
