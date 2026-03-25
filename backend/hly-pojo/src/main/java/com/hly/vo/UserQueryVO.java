package com.hly.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryVO {
    private Integer id;
    private String username;
    private String password;
    private String avatar;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String name;
    private Integer role;
    private Integer gender;
    private String bio;
    private String email;
    
}
