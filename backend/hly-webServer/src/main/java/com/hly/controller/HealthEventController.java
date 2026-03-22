package com.hly.controller;

import com.hly.dto.HealthEventInsertDTO;
import com.hly.result.Result;
import com.hly.service.HealthEventService;
import com.hly.utils.ThreadLocalUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hy/event")
@Slf4j
public class HealthEventController {

    @Autowired
    private HealthEventService eventService;
    
    @PostMapping
    @ApiModelProperty("新增健康事件")
    public Result insert(@RequestBody HealthEventInsertDTO healthEventInsertDTO){
        log.info("用户{}新增健康事件：{}", ThreadLocalUtil.getCurrentIdS(), healthEventInsertDTO);
        eventService.insert(healthEventInsertDTO);
        return Result.success();
    }
}
