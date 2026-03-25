package com.hly.service.impl;

import com.hly.constant.UIXConstant;
import com.hly.mapper.UIXMapper;
import com.hly.service.UIXService;
import com.hly.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UIXServiceImpl implements UIXService {
    
    @Autowired
    UIXMapper uixMapper;
    
    @Override
    public String follow(Integer followingId){
        if(followingId == ThreadLocalUtil.getCurrentIdS()) return UIXConstant.FOLLOW_SELF;
        else return null;
    }
}
