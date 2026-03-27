package com.hly.service;

import com.hly.dto.HealthEventPageAllDTO;
import com.hly.dto.HealthEventPageDTO;
import com.hly.result.PageResult;
import com.hly.vo.UserFollowQueryVO;

public interface UIXService {
    String follow(Integer followingId);
    
    UserFollowQueryVO followQuery(Integer followingId);
    
    void cancelFollow(Integer followingId);
    
    Integer getFollowCount(Integer id);
    
    Integer getFansCount(Integer id);
    
    PageResult pageQuery(HealthEventPageAllDTO healthEventPageAllDTO);
}
