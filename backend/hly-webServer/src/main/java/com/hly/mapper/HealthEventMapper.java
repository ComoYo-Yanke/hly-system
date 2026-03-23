package com.hly.mapper;

import com.hly.entity.HealthEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthEventMapper {
    
    @Insert("insert into health_event values (#{userId},#{eventType},#{content},#{eventTime},#{isPublic}," +
            "#{createdTime},#{updatedTime})")
    void insert(HealthEvent healthEvent);
}
