package com.hly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hly.constant.DataConstant;
import com.hly.dto.HealthEventInsertDTO;
import com.hly.dto.HealthEventPageDTO;
import com.hly.dto.HealthEventPageMapperDTO;
import com.hly.entity.HealthEvent;
import com.hly.mapper.HealthEventMapper;
import com.hly.result.PageResult;
import com.hly.service.HealthEventService;
import com.hly.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class HealthEventServiceImpl implements HealthEventService {
    
    @Autowired
    private HealthEventMapper healthEventMapper;
    
    /**
     * 添加用户健康事件
     * @param healthEventInsertDTO
     */
    @Override
    public void insert(HealthEventInsertDTO healthEventInsertDTO){
        HealthEvent healthEvent = new HealthEvent();
        BeanUtils.copyProperties(healthEventInsertDTO, healthEvent);
        
        healthEvent.setUserId(ThreadLocalUtil.getCurrentIdS());
        healthEvent.setCreatedTime(LocalDateTime.now());
        healthEvent.setUpdatedTime(LocalDateTime.now());
        healthEventMapper.insert(healthEvent);
    }
    
    /**
     * 用户健康事件分页查询
     * @param healthEventPageDTO
     * @return
     */
    @Override
    public PageResult pageQuery(HealthEventPageDTO healthEventPageDTO){
        // 使用多个DTO确保数据安全
        HealthEventPageMapperDTO healthEventPageMapperDTO = new HealthEventPageMapperDTO();
        BeanUtils.copyProperties(healthEventPageDTO, healthEventPageMapperDTO);
        // 保证用户只能查询自己的健康事件
        healthEventPageMapperDTO.setUserId(ThreadLocalUtil.getCurrentIdS());
        // 初始化数据处理
        if(healthEventPageMapperDTO.getPage() == null)
            healthEventPageMapperDTO.setPage(DataConstant.DEFAULT_PAGE);
        if(healthEventPageMapperDTO.getPageSize() == null)
            healthEventPageMapperDTO.setPageSize(DataConstant.DEFAULT_PAGE_SIZE);
        // 分页查询
        PageHelper.startPage(healthEventPageMapperDTO.getPage(), healthEventPageMapperDTO.getPageSize());
        log.info("分页查询：{}", healthEventPageMapperDTO);
        Page<HealthEvent> page = healthEventMapper.pageQuery(healthEventPageMapperDTO);
        long total = page.getTotal();
        List<HealthEvent> records = page.getResult();
        return new PageResult(total, records);
    }
}
