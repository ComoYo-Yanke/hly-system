package com.hly.controller;

import com.hly.result.Result;
import com.hly.service.UIXService;
import com.hly.vo.UIXFollowQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uix")
public class UIXController {
    
    @Autowired
    UIXService uixService;
    
//    @GetMapping("")
//    public Result<UIXFollowQueryVO> followQuery(){
//
//    }
    
    @PostMapping("/{followingId}")
    public Result follow(@PathVariable Integer followingId){
        
        String msg = uixService.follow(followingId);
        if(msg == null){
            return Result.success();
        }else {
            return Result.error(msg);
        }
        
        
    }
}
