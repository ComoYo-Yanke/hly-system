

# 需求
  ### 项目基础信息

  项目名称: 健康管理系统 (web版) v1.0
  用途: 记录用户健康情况, 包括但不限于每日早中午饭情况, 喝水情况, 起床和入睡情况, 运动情况
  技术栈: vue3 + node.js + mysql + springboot + java21 
  开发周期: 30 - 90天

  ### 核心功能
  - 用户模块: 登录, 注册, 退出登录, 修改密码, 注销
  - 健康管理模块: 添加通用健康事件(运动,步数,动态...),添加固定健康事件(睡眠,月经周期,孕期,运动类型--根据枚举选择类型,并进行约束:如一天睡觉时长不能超过24小时等),编辑健康事件,删除健康事件,查看健康事件列表,生成健康推荐,新增健康提醒,修改健康提醒,删除健康提醒,查询健康提醒
  - 用户互动模块: 用户之间可以通过id进行相互查询, 并进行关注, 可在主页查看用户公开的运动事件, 关注了的用户可在个人中心快速查看无需再次查询

  ### 页面清单
  登录页, 注册页, 健康事件页, 健康提醒设置页, 健康推荐页, 个人中心页

  ### 约束条件
  不做: 多人协作, 支付功能, 复杂权限管理
  截止标准: 核心功能能正常使用,无明显bug, 无需过渡优化(后期迭代)

  根据提供的需求文档，我对接口部分进行了补全和优化，确保符合 RESTful 设计风格，同时保留了所有核心功能。主要调整包括：

- **统一接口前缀**：所有接口以 `/api` 开头，模块划分清晰。
- **规范 HTTP 方法**：增删改查分别使用 POST/DELETE/PUT/GET，关注/取消关注使用 POST/DELETE 替代原来的重复 PUT。
- **完善参数与返回结构**：为每个接口补充了详细的请求参数（路径、查询、请求体）和统一格式的响应示例（code + data/msg）。
- **新增缺失接口**：补充了健康推荐、健康提醒主动触发、用户互动中的关注列表/关注数查询等。
- **明确鉴权方式**：所有需登录接口在请求头中携带 `Authorization: Bearer {token}`。

以下是完整的接口文档，改动部分已用注释说明，核心功能保持不变。

---

# 接口文档

## 通用说明

- **基础URL**：`http://<server>/api`
- **响应格式**：所有接口均返回 JSON，结构如下：
  ```json
  {
    "code": 200,          // 状态码，具体含义见通用响应码
    "data": {},           // 成功时的数据，可能为对象或数组
    "msg": "成功"          // 提示信息
  }
  ```
- **鉴权**：除登录、注册外，其他接口均需在请求头中携带 `Authorization: Bearer {token}`。
- **时间格式**：统一采用 `YYYY-MM-DD HH:mm:ss`。

---

## 通用响应码

| Code   | 说明                     |
|--------|--------------------------|
| 200    | 成功                     |
| 400    | 拒绝请求（用户信息错误） |
| 401000 | 拒绝请求（token失效）    |
| 401001 | 成功（登出成功）         |
| 401004 | 拒绝请求（重复登录）     |
| 403    | 拒绝请求（输入信息错误） |
| 404    | 拒绝请求（数据不存在）   |

---

## 1. 用户模块接口

### 1.1 用户登录
- **URL**：`/user/login`
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
    { "code": 200, "token": "eyJhbGc...", "msg": "登录成功" }
    ```
  - 失败：
    ```json
    { "code": 400, "msg": "用户名或密码错误" }
    ```

### 1.2 用户注册
- **URL**：`/user/register`
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
    { "code": 200, "msg": "注册成功，请重新登录" }
    ```
  - 失败（用户名已存在）：
    ```json
    { "code": 400, "msg": "用户名已存在" }
    ```

### 1.3 退出登录
- **URL**：`/user/logout`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  { "code": 401001, "msg": "退出成功" }
  ```

### 1.4 更改用户基本信息
// TODO 后续用户基本信息可更改

- **URL**：`/user`
- **方法**：PUT
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：
  ```json
  {
    "password":"string 可选", 
    "avatar":"string 可选",
    "name":"string 可选"
    
  }
  ```
- **响应示例**：
  ```json
  { "code": 401001, "msg": "修改成功" }
  ```

---

## 2. 健康事件接口

### 2.1 新增健康事件
- **URL**：`/hy/event`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "eventType": "string（必填，事件类型，如运动、睡眠等）",
    "content": "string（可选，事件详情）",
    "eventTime": "string（必填，格式：YYYY-MM-DD HH:mm:ss）",
    "isPublic": 1   // 必填，0-私密，1-公开，默认1
  }
  ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "新增成功" }
  ```

### 2.2 获取健康事件列表
- **URL**：`/hy/event`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（Query）：
  - `page`：页码，默认1
  - `size`：每页条数，默认20
  - `startTime`：开始时间（可选）
  - `endTime`：结束时间（可选）
  - `eventType`：事件类型筛选（可选）
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "total": 100,
      "list": [
        {
          "id": 1,
          "eventType": "运动",
          "content": "跑步5公里",
          "eventTime": "2025-03-20 18:30:00",
          "isPublic": 1
        }
      ]
    },
    "msg": "查询成功"
  }
  ```

### 2.3 根据ID查询单个健康事件
- **URL**：`/hy/event/{id}`
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
    "msg": "查询成功"
  }
  ```

### 2.4 修改健康事件
- **URL**：`/hy/event/{id}`
- **方法**：PUT
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：
  - 路径参数 `id`（事件ID）
  - JSON Body（至少包含一个要修改的字段）：
    ```json
    {
      "eventType": "string（可选）",
      "content": "string（可选）",
      "eventTime": "string（可选）",
      "isPublic": 0/1（可选）
    }
    ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "修改成功" }
  ```

### 2.5 删除健康事件
- **URL**：`/hy/event/{id}`
- **方法**：DELETE
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（事件ID）
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "删除成功" }
  ```

### 2.6 健康事件类型统计查询
- **URL**：`/hy/event/statistics`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（Query）：
  - `startDate`：开始日期（可选，YYYY-MM-DD）
  - `endDate`：结束日期（可选）
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": [
      { "eventType": "运动", "count": 10 },
      { "eventType": "睡眠", "count": 15 }
    ],
    "msg": "查询成功"
  }
  ```

---

## 3. 健康提醒接口

> 提醒字段说明：`remindType`（类型，如喝水、吃药）、`remindTime`（提醒时间 HH:mm）、`content`（提醒内容）、`repeatType`（重复类型：none-不重复，daily-每天，weekly-每周，monthly-每月）、`isActive`（是否启用）。

### 3.1 新增健康提醒
- **URL**：`/hy/remind`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（JSON Body）：
  ```json
  {
    "remindType": "string（必填）",
    "remindTime": "string（必填，HH:mm）",
    "content": "string（可选）",
    "repeatType": "string（可选，默认none）",
    "isActive": 1      // 0-禁用，1-启用，默认1
  }
  ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "新增成功" }
  ```

### 3.2 获取健康提醒列表
- **URL**：`/hy/remind`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（Query）：
  - `page`：页码，默认1
  - `size`：每页条数，默认20
  - `isActive`：筛选启用/禁用（可选）
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "total": 5,
      "list": [
        {
          "id": 1,
          "remindType": "喝水",
          "remindTime": "09:00",
          "content": "喝一杯水",
          "repeatType": "daily",
          "isActive": 1
        }
      ]
    },
    "msg": "查询成功"
  }
  ```

### 3.3 根据ID查询单个健康提醒
- **URL**：`/hy/remind/{id}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`（提醒ID）
- **响应示例**：同列表项结构。

### 3.4 修改健康提醒
- **URL**：`/hy/remind/{id}`
- **方法**：PUT
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：
  - 路径参数 `id`
  - JSON Body（至少一个字段）：
    ```json
    {
      "remindType": "string",
      "remindTime": "HH:mm",
      "content": "string",
      "repeatType": "string",
      "isActive": 0/1
    }
    ```
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "修改成功" }
  ```

### 3.5 删除健康提醒
- **URL**：`/hy/remind/{id}`
- **方法**：DELETE
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "删除成功" }
  ```

### 3.6 主动提醒
- **URL**：`/hy/remind/{id}/trigger`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `id`
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "提醒已触发" }
  ```

---

## 4. 健康推荐接口（新增）

- **URL**：`/hy/recommend`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": [
      { "type": "运动", "content": "今日建议跑步30分钟" },
      { "type": "喝水", "content": "记得喝8杯水" }
    ],
    "msg": "生成成功"
  }
  ```

---

## 5. 用户互动接口

> 所有接口均需登录。

### 5.1 查询用户信息
- **URL**：`/user/opt/info`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（Query）：
  - `userId`：可选，若不传则查询当前登录用户信息；若传则查询指定用户。
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "username": "张三",
      "avatar": "url",
      "followersCount": 10,
      "followingCount": 5
    },
    "msg": "查询成功"
  }
  ```

### 5.2 关注用户
- **URL**：`/user/opt/follow/{userId}`
- **方法**：POST
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `userId`（要关注的用户ID）
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "关注成功" }
  ```

### 5.3 取消关注用户
- **URL**：`/user/opt/follow/{userId}`
- **方法**：DELETE
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：路径参数 `userId`
- **响应示例**：
  ```json
  { "code": 200, "data": null, "msg": "取消关注成功" }
  ```

### 5.4 查询关注用户列表
- **URL**：`/user/opt/following`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**（Query）：
  - `page`：页码，默认1
  - `size`：每页条数，默认20
- **响应示例**：
  ```json
  {
    "code": 200,
    "data": {
      "total": 8,
      "list": [
        { "id": 2, "username": "李四", "avatar": "url" }
      ]
    },
    "msg": "查询成功"
  }
  ```

### 5.5 查询关注用户数
- **URL**：`/user/opt/following/count`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：无
- **响应示例**：
  ```json
  { "code": 200, "data": 8, "msg": "查询成功" }
  ```

### 5.6 查询指定用户的公开健康事件
- **URL**：`/user/opt/events/{userId}`
- **方法**：GET
- **请求头**：`Authorization: Bearer {token}`
- **请求参数**：
  - 路径参数 `userId`
  - Query：`page`、`size`（可选）
- **响应示例**：同 2.2 节列表结构，但只返回该用户的公开事件。

---

## 6. 管理员接口
// TODO 后续添加管理员接口,SpringSecurity

// TODO 后续添加AI模型接口,作用于快速生成健康建议与帖子内容

## 7. 页面路由与接口对应关系（参考）

| 页面               | 涉及接口                                                     |
|--------------------|--------------------------------------------------------------|
| 登录页             | 1.1 登录                                                     |
| 注册页             | 1.2 注册                                                     |
| 健康事件页         | 2.1~2.6 所有健康事件接口                                      |
| 健康提醒设置页     | 3.1~3.6 所有健康提醒接口                                      |
| 健康推荐页         | 4 健康推荐接口                                                |
| 个人中心页         | 5.1 查询当前用户信息；5.4 关注列表；5.5 关注数；5.6 他人公开事件 |

---

以上接口文档已按需求补全，并对 URL、方法进行了优化，确保功能完整且符合 RESTful 设计。如有疑问，欢迎讨论。