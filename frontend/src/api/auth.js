// /src/api/auth.js
import request from '@/utils/request'

// 登录
export const login = (username, password) => {
    console.log('发送登录请求:', { username, password })
    return request.post('/user/login', { username, password })
}

// 注册
export const register = (username, password) => {
    console.log('发送注册请求:', { username, password })
    return request.post('/user/register', { username, password })
}

// 登出
export const logout = () => {
    return request.post('/user/logout', {})
}

export default request