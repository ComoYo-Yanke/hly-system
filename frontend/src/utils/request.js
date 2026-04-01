import axios from "axios";

const request = axios.create({
    baseURL: '/api',
    timeout: 600000
});

request.interceptors.response.use(
    (response) => { //成功回调
        return response.data
    },
    (error) => { //失败回调
        return Promise.reject(error)
    }
)

export default request;