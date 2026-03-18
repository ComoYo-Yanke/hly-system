package com.hly.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageDTO {
    @ApiModelProperty("姓名关键字")
    private String name;
    @ApiModelProperty("性别")
    private Integer gender;
    @ApiModelProperty("页索引")
    private Integer page;
    @ApiModelProperty("单页显示数据量")
    private Integer pageSize;
}
