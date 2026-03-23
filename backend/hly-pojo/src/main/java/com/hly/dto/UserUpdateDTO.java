package com.hly.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @ApiModelProperty("用户id")
    private Integer id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("头像_URL")
    private String avatar;
    @ApiModelProperty("性别")
    private Integer gender;
    @ApiModelProperty("个性签名")
    private String bio;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("状态") // 0启用， 1禁用
    private String status;
    @ApiModelProperty("角色")
    private Integer role;
}
