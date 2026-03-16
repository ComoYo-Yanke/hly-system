package com.hly.constant;

import lombok.Data;


@Data
public class InfoConstant {
    public static final String RESPONSE_SUCCESS = "响应成功";
    public static final String RESPONSE_401000 = "登录令牌失效";
    public static final String RESPONSE_401001 = "登出";
    public static final String RESPONSE_401004 = "重复登录";
    public static final String ALREADY_EXISTS = "已存在";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final Integer DISABLE = 0;
    public static final Integer ENABLE  = 1;
    public static final String ACCOUNT_LOCKED = "账户被禁用";
    public static final String UNKNOWN_ERROR = "未知错误";
}
