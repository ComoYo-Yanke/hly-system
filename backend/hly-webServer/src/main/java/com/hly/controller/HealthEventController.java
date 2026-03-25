package com.hly.controller;

import com.hly.dto.HealthEventInsertDTO;
import com.hly.dto.HealthEventPageDTO;
import com.hly.dto.HealthEventUpdateDTO;
import com.hly.result.PageResult;
import com.hly.result.Result;
import com.hly.service.HealthEventService;
import com.hly.utils.ThreadLocalUtil;
import com.hly.vo.HealthEventQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hy/event")
@Slf4j
public class HealthEventController {

    @Autowired
    private HealthEventService healthEventService;
    
    @PostMapping
    @ApiModelProperty("新增健康事件")
    public Result insert(@RequestBody HealthEventInsertDTO healthEventInsertDTO){
        log.info("用户{}新增健康事件：{}", ThreadLocalUtil.getCurrentIdS(), healthEventInsertDTO);
        
        healthEventService.insert(healthEventInsertDTO);
        return Result.success();
    }
    
    @GetMapping
    @ApiModelProperty("健康事件分页查询")
    public Result<PageResult> page(@RequestBody HealthEventPageDTO healthEventPageDTO){
        log.info("分页查询：{}", healthEventPageDTO);
        PageResult pageResult = healthEventService.pageQuery(healthEventPageDTO);
        return Result.success(pageResult);
    }
    
    @PutMapping
    @ApiModelProperty("更新用户某个健康事件")
    public Result update(@RequestBody HealthEventUpdateDTO healthEventUpdateDTO){
        log.info("更新健康事件：{}", healthEventUpdateDTO);
        healthEventService.update(healthEventUpdateDTO);
        return Result.success();
    }
    
    @GetMapping("/{id}")
    @ApiModelProperty("健康事件回显")
    public Result<HealthEventQueryVO> queryById(@PathVariable Integer id){
        log.info("根据id查询健康事件：{}", id);
        HealthEventQueryVO healthEventQueryVO = healthEventService.queryById(id);
        
        return Result.success(healthEventQueryVO);
    }
    
    @DeleteMapping
    public Result delete(){
        return Result.success();
    }
}
