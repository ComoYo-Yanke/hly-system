package com.hly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hly.constant.DataConstant;
import com.hly.dto.HealthEventInsertDTO;
import com.hly.dto.HealthEventPageDTO;
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
    
    @Override
    public void insert(HealthEventInsertDTO healthEventInsertDTO){
        HealthEvent healthEvent = new HealthEvent();
        BeanUtils.copyProperties(healthEventInsertDTO, healthEvent);
        
        healthEvent.setCreatedTime(LocalDateTime.now());
        healthEvent.setUpdatedTime(LocalDateTime.now());
        healthEventMapper.insert(healthEvent);
    }
    
    @Override
    public PageResult pageQuery(HealthEventPageDTO healthEventPageDTO){
        if(healthEventPageDTO.getPage() == null) healthEventPageDTO.setPage(DataConstant.DEFAULT_PAGE);
        if(healthEventPageDTO.getPageSize() == null) healthEventPageDTO.setPageSize(DataConstant.DEFAULT_PAGE_SIZE);
        PageHelper.startPage(healthEventPageDTO.getPage(), healthEventPageDTO.getPageSize());
        log.info("分页查询：{}", healthEventPageDTO);
        Page<HealthEvent> page = healthEventMapper.pageQuery(healthEventPageDTO);
        long total = page.getTotal();
        List<HealthEvent> records = page.getResult();
        return new PageResult(total, records);
        
    }
}
