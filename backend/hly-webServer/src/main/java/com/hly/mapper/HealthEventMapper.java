package com.hly.mapper;

import com.github.pagehelper.Page;
import com.hly.dto.HealthEventPageMapperDTO;
import com.hly.entity.HealthEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthEventMapper {
    
    @Insert("insert into health_event values (#{userId},#{eventType},#{content},#{eventTime},#{isPublic}," +
            "#{createdTime},#{updatedTime})")
    void insert(HealthEvent healthEvent);
    
    Page<HealthEvent> pageQuery(HealthEventPageMapperDTO healthEventPageMapperDTO);
}
