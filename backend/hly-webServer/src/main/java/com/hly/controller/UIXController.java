package com.hly.controller;

import com.hly.result.PageResult;
import com.hly.result.Result;
import com.hly.service.UIXService;
import com.hly.vo.UserFollowQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.UserSessionEvent;

@RestController
@RequestMapping("/api/uix")
@Slf4j
public class UIXController {
    
    @Autowired
    UIXService uixService;
    
    @GetMapping("/{followingId}")
    @ApiModelProperty("获取当前用户对某用户的关注状态")
    public Result<UserFollowQueryVO> followQuery(@PathVariable Integer followingId){
        log.info("获取当前用户对用户{}的关注状态", followingId);
        UserFollowQueryVO userFollowQueryVO = uixService.followQuery(followingId);
        return Result.success(userFollowQueryVO);
    }
    
    @PostMapping("/{followingId}")
    @ApiModelProperty("关注用户")
    public Result follow(@PathVariable Integer followingId){
        
        log.info("关注{}用户", followingId);
        String msg = uixService.follow(followingId);
        if(msg == null){
            return Result.success();
        }else {
            return Result.error(msg);
        }
    }
    
    @DeleteMapping("/{followingId}")
    @ApiModelProperty("取消关注")
    public Result cancelFollow(@PathVariable Integer followingId){
        log.info("取消对{}用户的关注", followingId);
        
        uixService.cancelFollow(followingId);
        
        return Result.success();
    }
    
    @GetMapping("/fan/{id}")
    @ApiModelProperty("获取指定用户的粉丝数量")
    public Result<Integer> getFansCount(@PathVariable Integer id){
        log.info("获取用户{}的粉丝数", id);
        Integer count = uixService.getFansCount(id);
        
        return Result.success(count);
    }
    
    @GetMapping("/follow/{id}")
    @ApiModelProperty("获取指定用户的关注数量")
    public Result<Integer> getFollowsCount(@PathVariable Integer id){
        log.info("获取用户{}的关注数", id);
        Integer count = uixService.getFollowCount(id);
        
        return Result.success(count);
    }
    
}
