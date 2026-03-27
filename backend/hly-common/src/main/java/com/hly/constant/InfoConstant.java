package com.hly.constant;

import lombok.Data;

import javax.management.relation.InvalidRelationTypeException;


@Data
public class InfoConstant {
    public static final String RESPONSE_SUCCESS = "响应成功";
    public static final String RESPONSE_401000 = "登录令牌失效或无令牌";
    public static final String RESPONSE_401001 = "登出";
    public static final String RESPONSE_401004 = "重复登录";
    public static final String ALREADY_EXISTS = "已存在";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final Integer DISABLE = 0; // 禁用
    public static final Integer ENABLE  = 1; // 可用
    public static final String ACCOUNT_LOCKED = "账户被禁用";
    public static final String UNKNOWN_ERROR = "未知错误";
    public static final String NOT_FOUND_RESOURCE = "用户访问资源不存在";
    
    public static final Integer GENDER_MAN = 1; // 男
    public static final Integer GENDER_WOMAN = 2; // 女
    public static final Integer ROLE_NORMAL = 0; // 普通用户
    public static final Integer ROLE_ADMIN = 1; // 管理员
    public static final Integer EVENT_IS_PUBLIC = 1; // 公开事件
    public static final Integer EVENT_IS_PRIVATE = 0; // 私有事件
}
