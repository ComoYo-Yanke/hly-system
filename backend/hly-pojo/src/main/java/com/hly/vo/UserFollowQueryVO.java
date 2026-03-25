package com.hly.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowQueryVO {
    @ApiModelProperty("关注者")
    private Integer followerId;
    @ApiModelProperty("被关注者")
    private Integer followingId;
    @ApiModelProperty("关注时间")
    private LocalDateTime createdAT;
}
