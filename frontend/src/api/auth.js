import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
    baseURL: '/api',
    timeout: 10000,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器 - 添加 token
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        console.log('请求拦截器 - token:', token)
        console.log('请求URL:', config.url)
        
        // 登录和注册不需要添加 token
        if (token && !config.url.includes('/login') && !config.url.includes('/register')) {
            config.headers['Authorization'] = `Bearer ${token}`
            console.log('已添加 Authorization 头')
        }
        
        return config
    },
    error => {
        console.error('请求错误:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        console.log('响应数据:', response.data)
        return response.data
    },
    error => {
        console.error('响应错误:', error)
        
        if (error.code === 'ERR_NETWORK') {
            return Promise.reject(new Error('网络错误，无法连接到服务器'))
        }
        
        if (error.response) {
            if (error.response.status === 401) {
                console.log('收到401，清除token')
                localStorage.removeItem('token')
                localStorage.removeItem('userInfo')
                window.dispatchEvent(new CustomEvent('auth-unauthorized'))
                return Promise.reject(new Error('登录已过期，请重新登录'))
            }
            
            const message = error.response.data?.message || error.response.data?.msg || `请求失败 (${error.response.status})`
            return Promise.reject(new Error(message))
        }
        
        return Promise.reject(error)
    }
)

// 登录 - POST 请求
export const login = (username, password) => {
    console.log('发送登录请求:', { username, password })
    return request.post('/user/login', { username, password })
}

// 注册 - POST 请求
export const register = (username, password) => {
    console.log('发送注册请求:', { username, password })
    return request.post('/user/register', { username, password })
}

// 登出 - POST 请求，需要携带 token
export const logout = () => {
    const token = localStorage.getItem('token')
    console.log('登出请求，token:', token)
    return request.post('/user/logout', {})
}

export default request