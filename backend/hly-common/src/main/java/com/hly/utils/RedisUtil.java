package com.hly.utils;

import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
public class RedisUtil {
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    
    /**
     *  存用户id以及token
     *  key: "login:token:{Id}" value: "{token}"
     * @param id
     * @param token
     */
    public void setToken(Integer id, String token){
        log.info("存token：{}，{}", "login:token:" + id, token);
        redisTemplate.opsForValue().set("login:token:" + id.toString(), token);
    }
    
    /**
     * 根据用户id查询token
     * @param id
     * @return
     */
    public String getToken(Integer id) {
        return redisTemplate.opsForValue().get("login:token:" + id.toString()).toString();
    }
    
    /**
     * 根据用户id删除token
     * @param id
     * @return
     */
    public boolean deleteToken(Integer id) {
        return Boolean.TRUE.equals(redisTemplate.delete("login:token:" + id));
    }
}
