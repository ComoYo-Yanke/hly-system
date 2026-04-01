<template>
    <div class="main-layout">
        <!-- 侧边栏 -->
        <div class="sidebar aside">
            <div class="logo-area">
                <div class="logo-icon">
                    <svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M16 4L4 10L16 16L28 10L16 4Z" stroke="currentColor" stroke-width="1.5" fill="none"/>
                        <path d="M4 16L16 22L28 16" stroke="currentColor" stroke-width="1.5" fill="none"/>
                        <path d="M4 22L16 28L28 22" stroke="currentColor" stroke-width="1.5" fill="none"/>
                        <circle cx="16" cy="16" r="2" fill="currentColor"/>
                    </svg>
                </div>
                <h2 class="logo-text">Health<span>Flow</span></h2>
            </div>

            <el-menu 
                class="sidebar-menu" 
                mode="vertical" 
                router 
                :default-active="$route.path"
                :background-color="'transparent'"
                :text-color="'var(--aside-color)'"
                active-text-color="#fff"
                @select="handleMenuSelect">
                <el-menu-item index="/home">
                    <el-icon><HomeFilled /></el-icon>
                    <span>首页</span>
                </el-menu-item>
                <el-menu-item index="/user/info">
                    <el-icon><User /></el-icon>
                    <span>个人中心</span>
                </el-menu-item>
                <el-menu-item index="/health/event">
                    <el-icon><Notebook /></el-icon>
                    <span>健康事件</span>
                </el-menu-item>
                <el-menu-item index="/health/remind">
                    <el-icon><Bell /></el-icon>
                    <span>健康提醒</span>
                </el-menu-item>
                <el-menu-item index="/health-statistics">
                    <el-icon><DataAnalysis /></el-icon>
                    <span>健康统计</span>
                </el-menu-item>
                <el-menu-item index="/health-square">
                    <el-icon><Platform /></el-icon>
                    <span>健康广场</span>
                </el-menu-item>
                <el-menu-item index="/system/setting">
                    <el-icon><Setting /></el-icon>
                    <span>系统设置</span>
                </el-menu-item>
            </el-menu>

            <div class="sidebar-footer">
                <div class="version">v2.0.0</div>
            </div>
        </div>

        <!-- 右侧内容区 -->
        <div class="content-wrap">
            <!-- 顶部导航栏 -->
            <div class="header">
                <div class="header-left">
                    <div class="page-title">{{ currentPageTitle }}</div>
                </div>

                <div class="header-right">
                    <!-- 主题切换开关 -->
                    <div class="theme-switch" @click="toggleTheme">
                        <div class="switch-track" :class="{ active: isDark }">
                            <div class="switch-thumb">
                                <span v-if="!isDark">☀️</span>
                                <span v-else>🌙</span>
                            </div>
                        </div>
                    </div>

                    <!-- 用户信息下拉菜单 -->
                    <el-dropdown @command="handleUserCommand" trigger="click">
                        <div class="user-info">
                            <el-avatar :size="36" :icon="UserFilled" class="user-avatar" />
                            <div class="user-details">
                                <span class="user-name">{{ isLoggedIn ? (userInfo?.username || '健康使者') : '点击登录' }}</span>
                                <span class="user-role">{{ isLoggedIn ? (userInfo?.role || '') : '未登录' }}</span>
                            </div>
                        </div>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                                <el-dropdown-item command="settings">设置</el-dropdown-item>
                                <el-dropdown-item divided command="logout" v-if="isLoggedIn">退出登录</el-dropdown-item>
                                <el-dropdown-item command="login" v-else>登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>

            <!-- 主内容区域 - 无内边距 -->
            <div class="main-content">
                <!-- <router-view v-slot="{ Component }">
                    <transition name="fade-slide" mode="out-in">
                        <component :is="Component" />
                    </transition>
                </router-view> -->
                <router-view />
            </div>
        </div>

        <!-- 转场黑幕动画 - 立即触发 -->
        <div v-if="transitionActive" class="global-blackout">
            <div class="blackout-card">
                <div class="pulse-ring"></div>
                <div class="loading-logo">
                    <svg width="48" height="48" viewBox="0 0 32 32" fill="none">
                        <path d="M16 4L4 10L16 16L28 10L16 4Z" stroke="white" stroke-width="1.5" fill="none"/>
                        <path d="M4 16L16 22L28 16" stroke="white" stroke-width="1.5" fill="none"/>
                        <path d="M4 22L16 28L28 22" stroke="white" stroke-width="1.5" fill="none"/>
                        <circle cx="16" cy="16" r="2" fill="white"/>
                    </svg>
                </div>
                <div class="loading-text">加载中</div>
                <div class="loading-dots">
                    <span></span><span></span><span></span>
                </div>
            </div>
        </div>

        <!-- 登录提示弹窗 -->
        <el-dialog v-model="loginDialogVisible" title="提示" width="30%" :close-on-click-modal="false">
            <div style="text-align: center; padding: 20px;">
                <el-icon :size="60" color="#1677FF">
                    <Warning />
                </el-icon>
                <p style="margin-top: 16px; font-size: 16px;">请先登录后再访问此页面</p>
            </div>
            <template #footer>
                <el-button @click="loginDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleLogin">去登录</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
    HomeFilled, User, Notebook, Bell,
    DataAnalysis, Platform, Setting, UserFilled, Warning
} from '@element-plus/icons-vue'
import { logout } from '@/api/auth'

const router = useRouter()
const route = useRoute()
const isDark = ref(false)
const transitionActive = ref(false)
const isLoggedIn = ref(false)
const userInfo = ref(null)
const loginDialogVisible = ref(false)
let pendingRoute = null
let transitionTimer = null

// 检查登录状态
const checkLogin = () => {
    const token = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')
    
    console.log('检查登录状态 - token:', token)
    console.log('检查登录状态 - userInfo:', storedUserInfo)
    
    if (token && storedUserInfo) {
        isLoggedIn.value = true
        userInfo.value = JSON.parse(storedUserInfo)
        console.log('用户已登录:', userInfo.value)
    } else {
        isLoggedIn.value = false
        userInfo.value = null
        console.log('用户未登录')
    }
    return isLoggedIn.value
}

// 处理登出
// 处理登出
const handleLogout = async () => {
    try {
        const token = localStorage.getItem('token')
        console.log('登出时token:', token)
        
        const res = await logout()
        console.log('登出响应:', res)
        
        if (res.code === 200 || res.success === true) {
            ElMessage.success(res.message || '登出成功')
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
            isLoggedIn.value = false
            userInfo.value = null
            
            // 跳转到登录页
            router.push('/login')
        } else {
            ElMessage.error(res.message || '登出失败')
        }
    } catch (error) {
        console.error('登出错误:', error)
        ElMessage.error(error.message || '登出失败')
        // 即使接口失败，也清除本地存储
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        isLoggedIn.value = false
        userInfo.value = null
        router.push('/login')
    }
}

// 处理用户下拉菜单命令
const handleUserCommand = (command) => {
    switch (command) {
        case 'login':
            router.push('/login')
            break
        case 'logout':
            handleLogout()
            break
        case 'profile':
            if (!checkLogin()) {
                ElMessage.warning('请先登录')
                router.push('/login')
            } else {
                router.push('/user/info')
            }
            break
        case 'settings':
            if (!checkLogin()) {
                ElMessage.warning('请先登录')
                router.push('/login')
            } else {
                router.push('/system/setting')
            }
            break
    }
}

// 处理菜单选择
const handleMenuSelect = (index) => {
    // 首页不需要登录
    if (index === '/home' || index === '/') {
        startTransition(() => {
            router.push(index)
        })
        return
    }
    
    // 其他页面需要检查登录
    if (!checkLogin()) {
        pendingRoute = index
        loginDialogVisible.value = true
        return false
    }
    
    // 已登录，执行跳转
    startTransition(() => {
        router.push(index)
    })
}

// 启动转场动画（立即触发）
const startTransition = (callback) => {
    // 清除之前的定时器
    if (transitionTimer) clearTimeout(transitionTimer)
    
    transitionActive.value = true
    // 立即执行路由跳转
    callback()
    // 延迟关闭动画
    transitionTimer = setTimeout(() => {
        transitionActive.value = false
        transitionTimer = null
    }, 500)
}

// 处理登录（从弹窗跳转）
const handleLogin = () => {
    loginDialogVisible.value = false
    router.push({
        path: '/login',
        query: { redirect: route.fullPath }
    })
}

// 当前页面标题
const currentPageTitle = computed(() => {
    const titles = {
        '/home': '首页',
        '/user/info': '个人中心',
        '/health/event': '健康事件',
        '/health/remind': '健康提醒',
        '/health-statistics': '健康统计',
        '/health-square': '健康广场',
        '/system/setting': '系统设置'
    }
    return titles[route.path] || '健康管理系统'
})

// 切换主题
const toggleTheme = () => {
    isDark.value = !isDark.value
    const theme = isDark.value ? 'dark' : 'light'
    document.documentElement.setAttribute('data-theme', theme)
    localStorage.setItem('theme', theme)
}

// 监听未授权事件
const handleUnauthorized = () => {
    ElMessage.warning('登录已过期，请重新登录')
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    isLoggedIn.value = false
    userInfo.value = null
    
    if (route.path !== '/login') {
        router.push('/login')
    }
}

// 监听登录成功事件
const handleLoginSuccess = () => {
    checkLogin()
    // 如果有待跳转的页面，跳转过去
    if (pendingRoute) {
        startTransition(() => {
            router.push(pendingRoute)
            pendingRoute = null
        })
    }
}

onMounted(() => {
    const savedTheme = localStorage.getItem('theme') || 'light'
    isDark.value = savedTheme === 'dark'
    document.documentElement.setAttribute('data-theme', savedTheme)
    checkLogin()
    
    // 添加事件监听
    window.addEventListener('auth-unauthorized', handleUnauthorized)
    window.addEventListener('login-success', handleLoginSuccess)
})

onUnmounted(() => {
    window.removeEventListener('auth-unauthorized', handleUnauthorized)
    window.removeEventListener('login-success', handleLoginSuccess)
    if (transitionTimer) clearTimeout(transitionTimer)
})
</script>

<style scoped>
/* ========== 主布局样式 ========== */
*{
    user-select: none;
}
.main-layout {
    display: flex;
    min-height: 100vh;
    background: var(--main-bgc);
    position: relative;
    overflow-x: hidden;
}

/* 侧边栏 */
.sidebar {
    width: 280px;
    height: 100vh;
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    flex-direction: column;
    z-index: 100;
    overflow-y: auto;
    overflow-x: hidden;
}

/* 自定义侧边栏滚动条 */
.sidebar::-webkit-scrollbar {
    width: 4px;
}

.sidebar::-webkit-scrollbar-track {
    background: transparent;
}

.sidebar::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 4px;
}

/* Logo 区域 */
.logo-area {
    padding: 32px 24px;
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 8px;
    position: relative;
}

.logo-area::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 24px;
    right: 24px;
    height: 1px;
    background: linear-gradient(90deg, transparent, var(--aside-border), transparent);
}

.logo-icon {
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 14px;
    color: #fff;
    transition: all 0.3s ease;
}

.logo-area:hover .logo-icon {
    transform: scale(1.05);
    background: rgba(255, 255, 255, 0.25);
}

.logo-text {
    font-size: 20px;
    font-weight: 600;
    background: linear-gradient(135deg, #fff, rgba(255, 255, 255, 0.7));
    background-clip: text;
    -webkit-background-clip: text;
    color: transparent;
    letter-spacing: -0.5px;
}

.logo-text span {
    font-weight: 400;
    opacity: 0.8;
}

/* 菜单容器 */
.sidebar-menu {
    flex: 1;
    border: none;
    padding: 8px 12px;
    background: transparent !important;
}

/* 菜单项样式 */
:deep(.el-menu-item) {
    height: 48px;
    line-height: 48px;
    border-radius: 12px;
    margin-bottom: 4px;
    transition: all 0.3s ease;
    color: var(--aside-color) !important;
    opacity: 0.85;
}

:deep(.el-menu-item:hover) {
    opacity: 1;
    background: rgba(255, 255, 255, 0.1) !important;
    transform: translateX(4px);
}

:deep(.el-menu-item.is-active) {
    opacity: 1;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.18), rgba(255, 255, 255, 0.08)) !important;
    color: #fff !important;
}

:deep(.el-menu-item .el-icon) {
    font-size: 20px;
    margin-right: 12px;
}

/* 侧边栏底部 */
.sidebar-footer {
    padding: 20px 24px;
    position: relative;
}

.sidebar-footer::before {
    content: '';
    position: absolute;
    top: 0;
    left: 24px;
    right: 24px;
    height: 1px;
    background: linear-gradient(90deg, transparent, var(--aside-border), transparent);
}

.version {
    font-size: 12px;
    color: rgba(255, 255, 255, 0.45);
    letter-spacing: 1px;
    text-align: center;
}

/* 右侧内容区 */
.content-wrap {
    flex: 1;
    margin-left: 280px;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

/* 顶部导航栏 */
.header {
    position: sticky;
    top: 0;
    height: 70px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 32px;
    z-index: 99;
}

.header-left .page-title {
    font-size: 24px;
    font-weight: 600;
    background: linear-gradient(135deg, var(--header-color), var(--primary-color));
    background-clip: text;
    -webkit-background-clip: text;
    color: transparent;
    letter-spacing: -0.5px;
}

.header-right {
    display: flex;
    align-items: center;
    gap: 24px;
}

/* 主题切换开关 */
.theme-switch {
    cursor: pointer;
}

.switch-track {
    width: 56px;
    height: 30px;
    background: rgba(0, 0, 0, 0.2);
    border-radius: 30px;
    position: relative;
    transition: all 0.3s ease;
}

.switch-track.active {
    background: rgba(255, 255, 255, 0.2);
}

.switch-thumb {
    width: 26px;
    height: 26px;
    background: #fff;
    border-radius: 50%;
    position: absolute;
    top: 2px;
    left: 2px;
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.switch-track.active .switch-thumb {
    transform: translateX(26px);
}

/* 用户信息 */
.user-info {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 6px 16px 6px 12px;
    background: rgba(255, 255, 255, 0.08);
    border-radius: 40px;
    transition: all 0.3s ease;
    cursor: pointer;
    border: 1px solid rgba(255, 255, 255, 0.1);
}

.user-info:hover {
    background: rgba(255, 255, 255, 0.12);
    transform: translateY(-2px);
}

.user-avatar {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.user-details {
    display: flex;
    flex-direction: column;
}

.user-name {
    font-size: 14px;
    font-weight: 500;
    color: var(--header-color);
}

.user-role {
    font-size: 11px;
    color: rgba(255, 255, 255, 0.6);
}

/* 主内容区域 - 无内边距 */
.main-content {
    flex: 1;
    min-height: calc(100vh - 70px);
}

/* 页面切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
    transition: all 0.3s ease;
}

.fade-slide-enter-from {
    opacity: 0;
    transform: translateY(20px);
}

.fade-slide-leave-to {
    opacity: 0;
    transform: translateY(-20px);
}

/* ========== 转场黑幕动画 ========== */
.global-blackout {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.92);
    backdrop-filter: blur(12px);
    z-index: 9999;
    display: flex;
    align-items: center;
    justify-content: center;
    animation: blackoutFadeIn 0.3s ease-out;
}

@keyframes blackoutFadeIn {
    from {
        opacity: 0;
        backdrop-filter: blur(0);
    }
    to {
        opacity: 1;
        backdrop-filter: blur(12px);
    }
}

.blackout-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 24px;
    animation: scaleIn 0.4s cubic-bezier(0.34, 1.2, 0.64, 1);
}

.pulse-ring {
    position: absolute;
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
    animation: pulse 1.5s ease-out infinite;
}

.loading-logo {
    width: 80px;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 24px;
    animation: float 2s ease-in-out infinite;
    z-index: 1;
}

.loading-logo svg {
    animation: rotateLogo 1s linear infinite;
}

.loading-text {
    font-size: 18px;
    font-weight: 500;
    letter-spacing: 2px;
    color: #fff;
    text-transform: uppercase;
    animation: textGlow 1.5s ease-in-out infinite;
}

.loading-dots {
    display: flex;
    gap: 12px;
}

.loading-dots span {
    width: 8px;
    height: 8px;
    background: #fff;
    border-radius: 50%;
    animation: dotBounce 1s ease-in-out infinite;
}

.loading-dots span:nth-child(1) { animation-delay: 0s; }
.loading-dots span:nth-child(2) { animation-delay: 0.2s; }
.loading-dots span:nth-child(3) { animation-delay: 0.4s; }

/* 动画关键帧 */
@keyframes pulse {
    0% {
        transform: scale(0.8);
        opacity: 0.8;
    }
    100% {
        transform: scale(1.5);
        opacity: 0;
    }
}

@keyframes float {
    0%, 100% {
        transform: translateY(0);
    }
    50% {
        transform: translateY(-10px);
    }
}

@keyframes rotateLogo {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}

@keyframes textGlow {
    0%, 100% {
        text-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
    }
    50% {
        text-shadow: 0 0 30px rgba(255, 255, 255, 0.8);
    }
}

@keyframes dotBounce {
    0%, 100% {
        transform: scale(0.8);
        opacity: 0.4;
    }
    50% {
        transform: scale(1.2);
        opacity: 1;
    }
}

@keyframes scaleIn {
    from {
        opacity: 0;
        transform: scale(0.9);
    }
    to {
        opacity: 1;
        transform: scale(1);
    }
}

/* 响应式设计 */
@media (max-width: 768px) {
    .sidebar {
        width: 80px;
    }
    
    .logo-text,
    .sidebar-footer,
    :deep(.el-menu-item span) {
        display: none;
    }
    
    .logo-area {
        justify-content: center;
        padding: 24px 0;
    }
    
    .logo-area::after {
        left: 16px;
        right: 16px;
    }
    
    .content-wrap {
        margin-left: 80px;
    }
    
    .header {
        padding: 0 20px;
    }
    
    .user-details {
        display: none;
    }
    
    .main-content {
        min-height: calc(100vh - 70px);
    }
}
</style>