package com.hly.service;

import com.hly.dto.HealthEventCountDTO;
import com.hly.dto.HealthEventInsertDTO;
import com.hly.dto.HealthEventPageDTO;
import com.hly.dto.HealthEventUpdateDTO;
import com.hly.result.PageResult;
import com.hly.vo.HealthEventCountVO;
import com.hly.vo.HealthEventQueryVO;

import java.util.List;

public interface HealthEventService {
    void insert(HealthEventInsertDTO healthEventInsertDTO);
    
    PageResult pageQuery(HealthEventPageDTO healthEventPageDTO);
    
    void update(HealthEventUpdateDTO healthEventUpdateDTO);
    
    HealthEventQueryVO queryById(Integer id);
    
    void delete(Integer id);
    
    List<HealthEventCountVO> count(HealthEventCountDTO healthEventCountDTO);
}
