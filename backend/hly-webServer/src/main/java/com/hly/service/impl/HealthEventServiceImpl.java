package com.hly.service.impl;

import com.hly.dto.HealthEventInsertDTO;
import com.hly.entity.HealthEvent;
import com.hly.mapper.HealthEventMapper;
import com.hly.service.HealthEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HealthEventServiceImpl implements HealthEventService {
    
    @Autowired
    private HealthEventMapper healthEventMapper;
    
    @Override
    public void insert(HealthEventInsertDTO healthEventInsertDTO){
        HealthEvent healthEvent = new HealthEvent();
        BeanUtils.copyProperties(healthEventInsertDTO, healthEvent);
        
    }
}
