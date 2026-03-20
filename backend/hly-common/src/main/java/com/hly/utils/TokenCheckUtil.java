package com.hly.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenCheckUtil {
    
    /**
     * 检查token是否有重复
     * @param currentToken
     * @return
     */
    @Resource
    private RedisUtil redisUtil;
    public boolean tokenIsRepeated(String currentToken){
        Integer targetId = JwtUtil.getUserIdByToken(currentToken);
        String redisToken = redisUtil.getToken(targetId);
        
        if(!JwtUtil.twoOfTokensIsEqual(currentToken, redisToken))return true;
        else return false;
    }
}
