# 健康管理系统接口文档

## 项目介绍

健康管理系统是一个用于记录用户健康情况的Web应用，包括但不限于每日早中午饭情况、喝水情况、起床和入睡情况、运动情况等。

- **技术栈**：Vue3 + Node.js + MySQL + Spring Boot + Java 21
- **核心功能**：用户管理、健康事件管理、用户互动

## 通用说明

### 基础URL
- 所有接口均以 `/api` 开头

### 响应格式
所有接口均返回 JSON 格式，结构如下：

```json
{
  "code": 200,          // 状态码
  "data": {},           // 成功时的数据，可能为对象或数组
  "msg": "成功"          // 提示信息
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200    | 成功 |
| 400    | 拒绝请求（用户信息错误） |
| 401000 | 拒绝请求（token失效） |
| 401001 | 成功（登出成功） |
| 401004 | 拒绝请求（重复登录） |
| 403    | 拒绝请求（输入信息错误） |
| 404    | 拒绝请求（数据不存在） |

### 鉴权
- 除登录、注册外，其他接口均需在请求头中携带 `Authorization: Bearer {token}`

## 1. 用户模块接口

### 1.1 用户登录
- **URL**：`/api/user/login`
- **方法**：POST
- **请求参数**（JSON Body）：
  ```json
  {
    "username": "string（必填）",
    "password": "string（必填）"
  }
  ```
- **响应示例**：
  - 成功：
    ```json
    {
      "code": 200,
      "data": {
        "id": 1,
        "userName": "zhangsan",
        "name": "张三",
        "token": "eyJhbGc..."
      },
      "msg": "成功"
    }
    ```
  - 失败：
    ```json
    { "code": 400, "msg": "用户名或密码错误" }
    ```

### 1.2 用户注册
- **URL**：`/api/user/register`
- **方法**：POST
- **请求参数**（JSON Body）：
  ```json
  {
    "username": "string（必填）",
    "password": "string（必填）"
  }
  ```
- **响应示例**：
  - 成功：
    ```json
    { "code": 200, "data": null, "msg": "成功" }
    ```
  - 失败（用户名已存在）：
    ```json
    { "code": 400, "msg": "用户名已存在" }
    ```

### 1.3 退出登录
- **URL**：`/api/user/logout`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 1.4 更新用户基本信息
- **URL**：`/api/user`
- **方法**：PUT
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "id": "Integer 必填",
    "password": "string 可选",
    "avatar": "string 可选",
    "name": "string 可选",
    "gender": "int 可选"
  }
  ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 1.5 分页查询用户列表
- **URL**：`/api/user/page`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "name": "string 可选",
    "gender": "int 可选",
    "page": "int 可选",
    "pageSize": "int 可选"
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "total": 100,
      "rows": [
        {
          "id": 1,
          "name": "张三",
          "avatar": "url",
          "gender": 1
        }
      ]
    },
    "msg": "成功"
  }
  ```

### 1.6 注销账户
- **URL**：`/api/user/close`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 1.7 获取用户基本信息
- **URL**：`/api/user/{id}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（用户ID）
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "name": "张三",
      "avatar": "url",
      "gender": 1
    },
    "msg": "成功"
  }
  ```

### 1.8 查询用户粉丝数量
- **URL**：`/api/user/fan`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  { "code": 200, "data": 10, "msg": "成功" }
  ```

### 1.9 查询用户关注数量
- **URL**：`/api/user/follow`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  { "code": 200, "data": 5, "msg": "成功" }
  ```

## 2. 健康事件模块接口

### 2.1 新增健康事件
- **URL**：`/api/hy/event`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "eventType": "string（必填，事件类型）",
    "content": "string（可选，事件详情）",
    "eventTime": "string（必填，格式：YYYY-MM-DD HH:mm:ss）",
    "isPublic": 1  // 必填，0-私密，1-公开
  }
  ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 2.2 健康事件分页查询
- **URL**：`/api/hy/event`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "userId": "Integer 可选",
    "eventType": "string 可选",
    "startTime": "string 可选",
    "endTime": "string 可选",
    "page": "int 可选",
    "pageSize": "int 可选"
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "total": 100,
      "rows": [
        {
          "id": 1,
          "userId": 1,
          "eventType": "运动",
          "content": "跑步5公里",
          "eventTime": "2025-03-20 18:30:00",
          "isPublic": 1
        }
      ]
    },
    "msg": "成功"
  }
  ```

### 2.3 更新健康事件
- **URL**：`/api/hy/event`
- **方法**：PUT
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "id": "Integer 必填",
    "eventType": "string 可选",
    "content": "string 可选",
    "eventTime": "string 可选",
    "isPublic": "0/1 可选"
  }
  ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 2.4 根据ID查询健康事件
- **URL**：`/api/hy/event/{id}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（事件ID）
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "eventType": "运动",
      "content": "跑步5公里",
      "eventTime": "2025-03-20 18:30:00",
      "isPublic": 1
    },
    "msg": "成功"
  }
  ```

### 2.5 删除健康事件
- **URL**：`/api/hy/event/{id}`
- **方法**：DELETE
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（事件ID）
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 2.6 健康事件类型统计查询
- **URL**：`/api/hy/event/statistics`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "startDate": "string 可选，YYYY-MM-DD",
    "endDate": "string 可选"
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": [
      { "eventType": "运动", "count": 10 },
      { "eventType": "睡眠", "count": 15 }
    ],
    "msg": "成功"
  }
  ```

## 3. 用户互动模块接口

### 3.1 获取当前用户对某用户的关注状态
- **URL**：`/api/uix/{followingId}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `followingId`（被关注用户ID）
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "isFollow": true
    },
    "msg": "成功"
  }
  ```

### 3.2 关注用户
- **URL**：`/api/uix/{followingId}`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `followingId`（被关注用户ID）
- **响应示例**：
  - 成功：
    ```json
    { "code": 200, "data": null, "msg": "成功" }
    ```
  - 失败（已关注）：
    ```json
    { "code": 400, "msg": "已关注该用户" }
    ```

### 3.3 取消关注
- **URL**：`/api/uix/{followingId}`
- **方法**：DELETE
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `followingId`（被关注用户ID）
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "成功" }
  ```

### 3.4 获取指定用户的粉丝数量
- **URL**：`/api/uix/fan/{id}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（用户ID）
- **响应示例**：
  ```json
  { "code": 200, "data": 10, "msg": "成功" }
  ```

### 3.5 获取指定用户的关注数量
- **URL**：`/api/uix/follow/{id}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（用户ID）
- **响应示例**：
  ```json
  { "code": 200, "data": 5, "msg": "成功" }
  ```

### 3.6 获取所有公开健康事件
- **URL**：`/api/uix/hly/event`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "page": "int 可选",
    "pageSize": "int 可选",
    "eventType": "string 可选"
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "total": 100,
      "rows": [
        {
          "id": 1,
          "userId": 1,
          "eventType": "运动",
          "content": "跑步5公里",
          "eventTime": "2025-03-20 18:30:00",
          "isPublic": 1
        }
      ]
    },
    "msg": "成功"
  }
  ```

## 4. 接口使用示例

### 4.1 登录示例

```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username": "zhangsan", "password": "123456"}'
```

### 4.2 新增健康事件示例

```bash
curl -X POST http://localhost:8080/api/hy/event \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGc..." \
  -d '{"eventType": "运动", "content": "跑步5公里", "eventTime": "2025-03-20 18:30:00", "isPublic": 1}'
```

### 4.3 关注用户示例

```bash
curl -X POST http://localhost:8080/api/uix/2 \
  -H "Authorization: Bearer eyJhbGc..."
```

## 5. 注意事项

1. 所有接口均返回统一的JSON格式
2. 除登录和注册外，其他接口均需携带token
3. 时间格式统一使用 `YYYY-MM-DD HH:mm:ss`
4. 分页查询参数默认值：page=1, pageSize=10
5. 接口返回的data字段类型根据具体接口而定，可能是对象、数组或基本类型

## 6. 项目结构

- **后端**：Spring Boot + Java 21
- **前端**：Vue 3
- **数据库**：MySQL
- **认证**：JWT

## 7. 部署说明

1. 后端服务默认端口：8080
2. 前端服务默认端口：3000
3. 数据库连接配置在 `application.yml` 文件中
4. JWT密钥配置在 `JwtProperties` 类中

## 8. 版本信息

- 版本：v1.0.0
- 发布日期：2026-03-30
- 开发团队：健康管理系统开发团队
