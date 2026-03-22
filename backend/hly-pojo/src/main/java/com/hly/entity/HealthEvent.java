package com.hly.entity;

import com.hly.enumeration.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthEvent {
    private Integer id;
    private Integer userId;
    private String eventType;
    private String content;
    private LocalDateTime eventTime;
    @ApiModelProperty("是公开的")
    private Integer isPublic;// 1 为公开 0为私密
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    
}
