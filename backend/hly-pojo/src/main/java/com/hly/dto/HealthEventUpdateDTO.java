package com.hly.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthEventUpdateDTO {
    @ApiModelProperty("健康事件id")
    private Integer id;
    @ApiModelProperty("事件类型")
    private String eventType;
    @ApiModelProperty("事件描述")
    private String content;
    @ApiModelProperty("事件发生时间")
    private LocalDateTime eventTime;
    @ApiModelProperty("是公开的")
    private Integer isPublic;
}
