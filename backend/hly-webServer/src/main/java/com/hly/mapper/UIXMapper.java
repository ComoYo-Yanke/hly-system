package com.hly.mapper;

import com.hly.entity.UserFollow;
import com.hly.vo.UserFollowQueryVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UIXMapper {
    @Select("select * from user_follow where follower_id = #{followerId} and following_id = #{followingId}")
    UserFollowQueryVO query(UserFollow userFollow);
    
    @Insert("insert into user_follow (followerId, followingId, created_at)" +
            "values (#{follower_id}, #{following_id},#{createdAt})")
    void insert(UserFollow userFollow);
    
    void deleteById(UserFollow userFollow);
    
    void deleteById(Integer followerId, Integer followingId);
    
    Integer queryFollowCountOnlyOne(Integer id);
    
    Integer queryFansCountOnlyOne(Integer id);
}
