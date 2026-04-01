// /src/utils/request.js
import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router";

const request = axios.create({
    baseURL: '/api',
    timeout: 600000,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器 - 添加 token
request.interceptors.request.use(
    (config) => {
        // 从 localStorage 获取 token
        const token = localStorage.getItem('token');
        
        // 登录和注册请求不需要添加 token
        if (token && !config.url.includes('/login') && !config.url.includes('/register')) {
            config.headers['Authorization'] = `Bearer ${token}`;
            console.log('已添加 Authorization 头:', token);
        }
        if(token){
            config.headers.token = token
        }
        
        return config;
    },
    (error) => {
        console.error('请求错误:', error);
        return Promise.reject(error);
    }
);

// 响应拦截器 - 处理后端 Result 实体类
request.interceptors.response.use(
    (response) => {
        // 后端返回的 Result 实体类格式: { code, message, data }
        const result = response.data;
        console.log('响应数据:', result);
        
        // 如果 code 为 200 表示成功
        if (result.code === 200) {
            return result; // 直接返回 Result 对象
        } else {
            // 业务错误，如 401、403 等
            const errorMsg = result.msg || '请求失败';
            
            // 处理未授权的情况
            if (result.code === 401) {
                ElMessage.error(errorMsg);
                localStorage.removeItem('token');
                localStorage.removeItem('userInfo');
                router.push('/login');
            } else {
                ElMessage.error(errorMsg);
            }
            
            return Promise.reject(new Error(errorMsg));
        }
    },
    (error) => {
        console.error('响应错误:', error);
        
        // 网络错误
        if (error.code === 'ERR_NETWORK') {
            ElMessage.error('网络错误，无法连接到服务器');
            return Promise.reject(new Error('网络错误，无法连接到服务器'));
        }
        
        // HTTP 状态码错误
        if (error.response) {
            const status = error.response.status;
            const result = error.response.data;
            
            switch (status) {
                case 401:
                    ElMessage.error(error);
                    localStorage.removeItem('token');
                    localStorage.removeItem('userInfo');
                    router.push('/login');
                    break;
                case 403:
                    ElMessage.error('没有权限访问该资源');
                    break;
                case 404:
                    ElMessage.error('请求的资源不存在');
                    break;
                case 500:
                    ElMessage.error('服务器内部错误');
                    break;
                default:
                    const errorMsg = result?.message || `请求失败 (${status})`;
                    ElMessage.error(errorMsg);
            }
            
            return Promise.reject(error);
        }
        
        // 其他错误
        ElMessage.error(error.message || '请求失败');
        return Promise.reject(error);
    }
);

export default request;