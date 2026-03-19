package com.hly.mapper;

import com.github.pagehelper.Page;
import com.hly.dto.UserPageDTO;
import com.hly.entity.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username} limit 1")
    User getByUsername(String username);
    
    @Insert("insert into user" +
            "(username, password, avatar, status, created_time, updated_time, name, role)" +
            "values " +
            "(#{username}, #{password}, #{avatar}, #{status}, #{createdTime}, #{updatedTime}, #{name}, #{role})")
    void insert(User user);
    
    void updateById(User user);
    
    Page<User> pageQuery(UserPageDTO userPageDTO);
}
