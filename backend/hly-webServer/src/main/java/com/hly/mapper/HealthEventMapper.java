package com.hly.mapper;

import com.github.pagehelper.Page;
import com.hly.dto.HealthEventCountDTO;
import com.hly.dto.HealthEventPageAllDTO;
import com.hly.dto.HealthEventPageMapperDTO;
import com.hly.entity.HealthEvent;
import com.hly.vo.HealthEventCountVO;
import com.hly.vo.HealthEventQueryVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HealthEventMapper {
    
    @Insert("insert into health_event(user_id,event_type,content,event_time,is_public,created_time,updated_time)" +
            " values (#{userId},#{eventType},#{content},#{eventTime},#{isPublic}," +
            "#{createdTime},#{updatedTime})")
    void insert(HealthEvent healthEvent);
    
    Page<HealthEvent> pageQuery(HealthEventPageMapperDTO healthEventPageMapperDTO);
    
    void update(HealthEvent healthEvent);
    
    @Select("select * from health_event where id = #{id} and user_id = #{userId}")
    HealthEvent queryById(Integer id, Integer userId);
    
    @Delete("delete from health_event where id = #{id} and user_id = #{userId}")
    void delete(Integer id, Integer userId);
    
    List<HealthEventCountVO> count(HealthEventCountDTO healthEventCountDTO);
    
    Page<HealthEvent> pageAllQuery(HealthEventPageAllDTO healthEventPageAllDTO);
}
