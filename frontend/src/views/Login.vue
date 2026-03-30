<template>
    <MainLayout style="position: fixed;"></MainLayout>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <div class="logo-icon">
                    <svg width="48" height="48" viewBox="0 0 32 32" fill="none">
                        <path d="M16 4L4 10L16 16L28 10L16 4Z" stroke="currentColor" stroke-width="1.5" fill="none" />
                        <path d="M4 16L16 22L28 16" stroke="currentColor" stroke-width="1.5" fill="none" />
                        <path d="M4 22L16 28L28 22" stroke="currentColor" stroke-width="1.5" fill="none" />
                        <circle cx="16" cy="16" r="2" fill="currentColor" />
                    </svg>
                </div>
                <h2>HealthFlow</h2>
                <p>健康管理系统</p>
            </div>

            <el-tabs v-model="activeTab" class="login-tabs">
                <!-- 登录表单 -->
                <el-tab-pane label="登录" name="login">
                    <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
                        <el-form-item prop="username">
                            <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" size="large"
                                clearable>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="password">
                            <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock"
                                size="large" show-password>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="captcha">
                            <div class="captcha-wrapper">
                                <el-input v-model="loginForm.captcha" placeholder="验证码" prefix-icon="Key" size="large"
                                    style="flex: 1" maxlength="4">
                                </el-input>
                                <div class="captcha-image" @click="refreshCaptcha">
                                    <canvas ref="captchaCanvas" width="120" height="40"></canvas>
                                    <div class="refresh-icon">⟳</div>
                                </div>
                            </div>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary" size="large" :loading="loginLoading" @click="handleLogin"
                                style="width: 100%">
                                登录
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>

                <!-- 注册表单 -->
                <el-tab-pane label="注册" name="register">
                    <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef">
                        <el-form-item prop="username">
                            <el-input v-model="registerForm.username" placeholder="用户名 (3-20个字符)" prefix-icon="User"
                                size="large" clearable>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="password">
                            <el-input v-model="registerForm.password" type="password" placeholder="密码 (6-20个字符)"
                                prefix-icon="Lock" size="large" show-password>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="confirmPassword">
                            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码"
                                prefix-icon="Lock" size="large" show-password>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="captcha">
                            <div class="captcha-wrapper">
                                <el-input v-model="registerForm.captcha" placeholder="验证码" prefix-icon="Key"
                                    size="large" style="flex: 1" maxlength="4">
                                </el-input>
                                <div class="captcha-image" @click="refreshCaptchaRegister">
                                    <canvas ref="captchaCanvasRegister" width="120" height="40"></canvas>
                                    <div class="refresh-icon">⟳</div>
                                </div>
                            </div>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary" size="large" :loading="registerLoading" @click="handleRegister"
                                style="width: 100%">
                                注册
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup>
import MainLayout from '@/views/home/index.vue'
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '@/api/auth'

const router = useRouter()
const route = useRoute()
const activeTab = ref('login')
const loginLoading = ref(false)
const registerLoading = ref(false)
const loginFormRef = ref()
const registerFormRef = ref()
const captchaCanvas = ref(null)
const captchaCanvasRegister = ref(null)

// 验证码值
const captchaValue = ref('')
const captchaValueRegister = ref('')

// 登录表单
const loginForm = reactive({
    username: '',
    password: '',
    captcha: ''
})

// 注册表单
const registerForm = reactive({
    username: '',
    password: '',
    confirmPassword: '',
    captcha: ''
})

// 生成随机验证码
const generateCaptcha = () => {
    const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    let result = ''
    for (let i = 0; i < 4; i++) {
        result += chars[Math.floor(Math.random() * chars.length)]
    }
    return result
}

// 绘制验证码
const drawCaptcha = (canvas, code) => {
    if (!canvas) return

    const ctx = canvas.getContext('2d')
    const width = canvas.width
    const height = canvas.height

    ctx.clearRect(0, 0, width, height)
    ctx.fillStyle = '#f5f5f5'
    ctx.fillRect(0, 0, width, height)

    // 干扰线
    for (let i = 0; i < 8; i++) {
        ctx.beginPath()
        ctx.moveTo(Math.random() * width, Math.random() * height)
        ctx.lineTo(Math.random() * width, Math.random() * height)
        ctx.strokeStyle = `rgba(0, 0, 0, 0.1)`
        ctx.stroke()
    }

    // 干扰点
    for (let i = 0; i < 60; i++) {
        ctx.fillStyle = `rgba(0, 0, 0, 0.1)`
        ctx.fillRect(Math.random() * width, Math.random() * height, 1, 1)
    }

    // 文字
    for (let i = 0; i < code.length; i++) {
        const x = 15 + i * 25 + Math.random() * 5
        const y = 25 + Math.random() * 8
        ctx.font = `bold ${20 + Math.random() * 5}px Arial`
        ctx.fillStyle = `rgb(${Math.random() * 100}, ${Math.random() * 100}, ${Math.random() * 150})`
        ctx.fillText(code[i], x, y)
    }
}

// 刷新验证码
const refreshCaptcha = () => {
    const newCaptcha = generateCaptcha()
    captchaValue.value = newCaptcha
    nextTick(() => {
        drawCaptcha(captchaCanvas.value, newCaptcha)
    })
}

const refreshCaptchaRegister = () => {
    const newCaptcha = generateCaptcha()
    captchaValueRegister.value = newCaptcha
    nextTick(() => {
        drawCaptcha(captchaCanvasRegister.value, newCaptcha)
    })
}

// 验证验证码
const verifyCaptcha = (inputCaptcha, isRegister = false) => {
    const correctCaptcha = isRegister ? captchaValueRegister.value : captchaValue.value
    if (!inputCaptcha || inputCaptcha.toUpperCase() !== correctCaptcha.toUpperCase()) {
        ElMessage.error('验证码错误')
        if (isRegister) {
            refreshCaptchaRegister()
            registerForm.captcha = ''
        } else {
            refreshCaptcha()
            loginForm.captcha = ''
        }
        return false
    }
    return true
}

// 登录表单验证规则
const loginRules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
    ],
    captcha: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { len: 4, message: '验证码长度为4位', trigger: 'blur' }
    ]
}

// 注册表单验证规则
const registerRules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (value !== registerForm.password) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ],
    captcha: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { len: 4, message: '验证码长度为4位', trigger: 'blur' }
    ]
}

// 登录处理
const handleLogin = async () => {
    if (!loginFormRef.value) return

    await loginFormRef.value.validate(async (valid) => {
        if (!valid) return

        if (!verifyCaptcha(loginForm.captcha, false)) {
            return
        }

        loginLoading.value = true
        try {
            const res = await login(loginForm.username, loginForm.password)
            console.log('登录响应:', res)

            // 根据后端返回格式调整
            if (res.code === 200 || res.success === true) {
                // 存储 token - 根据后端返回的字段名调整
                const token = res.data?.token || res.token
                const userInfo = res.data?.userInfo || res.userInfo || { username: loginForm.username }

                if (token) {
                    localStorage.setItem('token', token)
                    localStorage.setItem('userInfo', JSON.stringify(userInfo))
                    console.log('Token已存储:', token)

                    ElMessage.success(res.message || '登录成功')

                    // 触发登录成功事件
                    window.dispatchEvent(new CustomEvent('login-success'))

                    // 跳转
                    const redirect = route.query.redirect || '/home'
                    setTimeout(() => {
                        router.push(redirect)
                    }, 500)
                } else {
                    ElMessage.error('登录成功但未返回token')
                }
            } else {
                ElMessage.error(res.message || '登录失败')
                refreshCaptcha()
                loginForm.captcha = ''
            }
        } catch (error) {
            console.error('登录错误:', error)
            ElMessage.error(error.message || '登录失败，请稍后重试')
            refreshCaptcha()
            loginForm.captcha = ''
        } finally {
            loginLoading.value = false
        }
    })
}

// 注册处理
const handleRegister = async () => {
    if (!registerFormRef.value) return

    await registerFormRef.value.validate(async (valid) => {
        if (!valid) return

        if (!verifyCaptcha(registerForm.captcha, true)) {
            return
        }

        registerLoading.value = true
        try {
            const res = await register(registerForm.username, registerForm.password)
            console.log('注册响应:', res)

            if (res.code === 200 || res.success === true) {
                ElMessage.success(res.message || '注册成功，请登录')

                registerForm.username = ''
                registerForm.password = ''
                registerForm.confirmPassword = ''
                registerForm.captcha = ''
                activeTab.value = 'login'
                refreshCaptcha()
            } else {
                ElMessage.error(res.message || '注册失败')
                refreshCaptchaRegister()
                registerForm.captcha = ''
            }
        } catch (error) {
            console.error('注册错误:', error)
            ElMessage.error(error.message || '注册失败，请稍后重试')
            refreshCaptchaRegister()
            registerForm.captcha = ''
        } finally {
            registerLoading.value = false
        }
    })
}

onMounted(() => {
    refreshCaptcha()
    refreshCaptchaRegister()
})
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    /* background: linear-gradient(135deg, #a8a9a97d 0%, #6b607769 100%); */
    position: relative;
    overflow: hidden;
    backdrop-filter: blur(15px);
}

.login-container::before {
    content: '';
    position: absolute;
    width: 200%;
    height: 200%;
    top: -50%;
    left: -50%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 1px, transparent 1px);
    background-size: 50px 50px;
    animation: moveBg 20s linear infinite;
}

@keyframes moveBg {
    0% {
        transform: translate(0, 0);
    }

    100% {
        transform: translate(50px, 50px);
    }
}

.login-card {
    width: 500px;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 24px;

    padding: 40px;
    box-shadow: 0 0 15px 5px rgba(0, 150, 255, 0.6);
    animation: slideUp 0.5s ease-out,
        glow 2s infinite alternate,
        pulse 1.5s infinite ease-in-out;
    position: relative;
    z-index: 1;
    transition: all 0.3s ease;
}

.login-card:hover {
    box-shadow: 0 0 20px 10px rgba(0, 150, 255, 0.8);
    animation: slideUp 0.5s ease-out,
        
        pulse 1.5s infinite ease-in-out;
    transition: all 0.3s ease;
}



@keyframes pulse {
    0% {
        box-shadow: 0 0 30px rgba(22, 137, 244, 0.26);
        transform: translateY(-5px);
    }

    50% {
        box-shadow: 0 0 80px rgba(102, 0, 255, 0.87);
        transform: translateY(0px);
    }

    100% {
        box-shadow: 0 0 30px rgba(91, 83, 240, 0.504);
        transform: translateY(-5px);
    }
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.login-header {
    text-align: center;
    margin-bottom: 32px;
}

.logo-icon {
    width: 64px;
    height: 64px;
    margin: 0 auto 16px;
    background: linear-gradient(135deg, #1677FF, #4096ff);
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
}

.login-header h2 {
    font-size: 28px;
    font-weight: 600;
    margin: 0 0 8px 0;
    background: linear-gradient(135deg, #1677FF, #4096ff);
    background-clip: text;
    -webkit-background-clip: text;
    color: transparent;
}

.login-header p {
    color: #666;
    margin: 0;
    font-size: 14px;
}

.login-tabs :deep(.el-tabs__header) {
    margin-bottom: 24px;
}

.login-tabs :deep(.el-tabs__item) {
    font-size: 16px;
    font-weight: 500;
}

.captcha-wrapper {
    display: flex;
    gap: 12px;
    align-items: center;
}

.captcha-image {
    width: 120px;
    height: 40px;
    cursor: pointer;
    position: relative;
    flex-shrink: 0;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid #e0e0e0;
}

.captcha-image canvas {
    width: 100%;
    height: 100%;
    display: block;
}

.refresh-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 20px;
    color: rgba(0, 0, 0, 0.6);
    opacity: 0;
    transition: opacity 0.3s;
    background: rgba(255, 255, 255, 0.9);
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.captcha-image:hover .refresh-icon {
    opacity: 1;
}

:deep(.el-form-item) {
    margin-bottom: 20px;
}

:deep(.el-input__wrapper) {
    border-radius: 12px;
    box-shadow: 0 0 0 1px #e0e0e0;
}

:deep(.el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px #1677FF;
}

:deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 1px #1677FF;
}

@media (max-width: 768px) {
    .login-card {
        width: 90%;
        padding: 32px 24px;
        margin: 20px;
    }
}
</style>