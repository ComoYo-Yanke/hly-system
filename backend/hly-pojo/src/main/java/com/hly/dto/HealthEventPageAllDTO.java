package com.hly.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthEventPageAllDTO {
    @ApiModelProperty("用户姓名")
    private String name;
    @ApiModelProperty("事件类型")
    private String eventType;
    @ApiModelProperty("事件发生时间区间")
    private LocalDateTime eventTimeBegan;
    private LocalDateTime eventTimeEnd;
    @ApiModelProperty("页索引")
    private Integer page;
    @ApiModelProperty("单页显示数据量")
    private Integer pageSize;
}
