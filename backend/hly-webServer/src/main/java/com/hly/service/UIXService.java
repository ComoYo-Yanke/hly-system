package com.hly.service;

import com.hly.vo.UserFollowQueryVO;

public interface UIXService {
    String follow(Integer followingId);
    
    UserFollowQueryVO followQuery(Integer followingId);
    
    void cancelFollow(Integer followingId);
    
    Integer getFollowCount(Integer id);
    
    Integer getFansCount(Integer id);
}
