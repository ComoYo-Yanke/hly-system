package com.hly.service;

import com.hly.dto.HealthEventInsertDTO;
import com.hly.dto.HealthEventPageDTO;
import com.hly.result.PageResult;

public interface HealthEventService {
    void insert(HealthEventInsertDTO healthEventInsertDTO);
    
    PageResult pageQuery(HealthEventPageDTO healthEventPageDTO);
}
