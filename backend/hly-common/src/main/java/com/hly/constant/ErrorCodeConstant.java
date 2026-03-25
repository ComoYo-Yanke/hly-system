package com.hly.constant;

import lombok.Data;

@Data
public class ErrorCodeConstant {
    public static final Integer COUNT_EXIST = 409;
    public static final Integer TOKEN_DISABLE = 401;
    
    public static final String TOKEN_EXPIRED = "{\"code\":401,\"msg\":\"登录已过期，请重新登录\"}";
    public static final String TOKEN_IS_NULL = "{\"code\":401,\"msg\":\"令牌为空\"}";
    public static final String TOKEN_IS_FALSE = "{\"code\":401,\"msg\":\"令牌非法\"}";
    public static final String IS_REPEAT_LOGIN = "{\"code\":401,\"msg\":\"重复登录\"}";
    public static final String NO_LOGIN_FINALLY = "{\"code\":401,\"msg\":\"用户未登录\"}";
    
    
}
