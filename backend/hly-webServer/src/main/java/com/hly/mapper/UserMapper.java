package com.hly.mapper;

import com.hly.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username} limit 1")
    User getByUsername(String username);
}
