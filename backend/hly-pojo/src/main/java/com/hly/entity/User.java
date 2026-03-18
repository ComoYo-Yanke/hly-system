package com.hly.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @ApiModelProperty("用户id")
    private Integer id;
    private String username;
    private String password;
    private String avatar;
    private Integer status; // 0 禁用 1 启用
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String name;
    private Integer role;
}
