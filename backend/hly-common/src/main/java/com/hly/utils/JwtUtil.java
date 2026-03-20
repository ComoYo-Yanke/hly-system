package com.hly.utils;

import java.nio.charset.StandardCharsets;

import com.hly.constant.JwtClaimsConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    
    private static final String SECRET_KEY = "comoyoyanke1234567890";
    private static final Long TTL_MILLIS = 1000 * 60 * 60L;
    
    /**
     * 生成JWT令牌
     *
     * @param ttlMillis
     * @param claims
     * @return
     */
    
    public static String createJWT(long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis * TTL_MILLIS;
        Date exp = new Date(expMillis);
        
        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);
        
        return builder.compact();
    }
    
    /**
     * 解析JWT令牌
     *
     * @param token
     * @return
     */
    public static Claims parseJWT(String token) {
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }
    
    // 通过用户token获取id
    public static Integer getUserIdByToken(String token){
        Claims claims = parseJWT(token);
        return claims.get(JwtClaimsConstant.ID, Integer.class);
    }
    
    // 对比两个token是否相等
    public static boolean twoOfTokensIsEqual(String token1, String token2){
        if(token1.equals(token2)) return true;
        else return false;
    }
}
