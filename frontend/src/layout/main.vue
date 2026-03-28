<template>
    <div class="main-layout">
        <!-- 侧边栏 -->
        <div class="sidebar aside">
            <h3 style="width: 100%; text-align: center; margin-top: 10px">健康管理系统</h3>

            <el-menu class="sidebar-menu" mode="vertical" router background-color="var(--aside-bgc)"
                text-color="var(--aside-color)" active-text-color="#fff"
                style="width: 100%; border: none; margin-top: 20px">
                <el-menu-item index="/">
                    <el-icon>
                        <HomeFilled />
                    </el-icon>
                    <span>首页</span>
                </el-menu-item>
                <el-menu-item index="/user/info">
                    <el-icon>
                        <User />
                    </el-icon>
                    <span>个人中心</span>
                </el-menu-item>
                <el-menu-item index="/health/event">
                    <el-icon>
                        <Notebook />
                    </el-icon>
                    <span>健康事件</span>
                </el-menu-item>
                <el-menu-item index="/health/remind">
                    <el-icon>
                        <Bell />
                    </el-icon>
                    <span>健康提醒</span>
                </el-menu-item>
                <el-menu-item index="/health-statistics">
                    <el-icon>
                        <DataAnalysis />
                    </el-icon>
                    <span>健康统计</span>
                </el-menu-item>
                <el-menu-item index="/health-square">
                    <el-icon>
                        <Platform />
                    </el-icon>
                    <span>健康广场</span>
                </el-menu-item>
                <el-menu-item index="/system-setting">
                    <el-icon>
                        <Setting />
                    </el-icon>
                    <span>系统设置</span>
                </el-menu-item>
            </el-menu>
        </div>

        <!-- 右侧内容 -->
        <div class="content-wrap">
            <div class="header">
                <div class="icon" style="width: 16%; height: 100%; white-space: nowrap">
                    <el-icon size="22px">
                        <Eleme />
                    </el-icon>
                    <span style="font-size: 180%">健康管理系统</span>
                </div>

                <!-- ✅ 1:1 网红逼疯前端主题开关 -->
                <div class="toggle-container" style="margin-left: auto">
                    <input type="checkbox" id="theme-toggle" :checked="isDark" @change="toggleTheme" hidden />
                    <label for="theme-toggle" class="toggle">
                        <div class="icon sun">
                            <div class="ray"></div>
                            <div class="ray"></div>
                            <div class="ray"></div>
                            <div class="ray"></div>
                            <div class="ray"></div>
                            <div class="ray"></div>
                            <div class="ray"></div>
                            <div class="ray"></div>
                        </div>
                        <div class="cloud"></div>
                        <div class="moon">
                            <div class="crater crater-1"></div>
                            <div class="crater crater-2"></div>
                        </div>
                        <div class="stars">
                            <div class="star"></div>
                            <div class="star"></div>
                            <div class="star"></div>
                            <div class="star"></div>
                        </div>
                    </label>
                </div>
            </div>

            <div class="content main">
                <router-view />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
    Eleme, HomeFilled, User, Notebook, Bell,
    DataAnalysis, Platform, Setting
} from '@element-plus/icons-vue'

const isDark = ref(false)

const toggleTheme = (e) => {
    const val = e.target.checked
    if (val) {
        document.documentElement.setAttribute('data-theme', 'dark')
        localStorage.setItem('theme', 'dark')
    } else {
        document.documentElement.setAttribute('data-theme', 'light')
        localStorage.setItem('theme', 'light')
    }
    isDark.value = val
}

onMounted(() => {
    const theme = localStorage.getItem('theme') || 'light'
    isDark.value = theme === 'dark'
    document.documentElement.setAttribute('data-theme', theme)
})
</script>

<style scoped>
/* 原有样式 100% 不动 */
* {
    transition: all 0.5s ease;
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

.main-layout {
    display: flex;
    height: 100vh;
    justify-content: start;
    position: relative;
    overflow: hidden;
}

.sidebar {
    width: 15%;
    height: 100%;
    user-select: none;
    position: fixed;
    display: flex;
    flex-direction: column;
    justify-content: start;
    z-index: 2;
    top: 0;
    left: 0;
}

.content-wrap {
    display: flex;
    flex-direction: column;
    width: 85%;
    height: 100%;
    margin-left: 15%;
}

.header {
    position: fixed;
    height: 60px;
    top: 0;
    width: 85%;
    line-height: 60px;
    padding: 0 20px;
    user-select: none;
    display: flex;
    align-items: center;
    z-index: 1;
}

.content {
    user-select: none;
    width: 100%;
    height: calc(100% - 60px);
    margin-top: 60px;
    overflow: auto;
}

/* 菜单效果 */
:deep(.el-menu-item:hover) {
    background-color: rgba(255, 255, 255, 0.15) !important;
}

:deep(.el-menu-item.is-active) {
    background-color: rgba(255, 255, 255, 0.25) !important;
    border-left: 3px solid #fff;
}

/* ————————————————————————————————
 ✅ 1:1 完美还原网红昼夜开关
———————————————————————————————— */
.toggle-container {
    display: flex;
    align-items: center;
    justify-content: center;
}

.toggle {
    position: relative;
    width: 60px;
    height: 28px;
    background: linear-gradient(135deg, #74bfff 0%, #a8d8ff 100%);
    border-radius: 50px;
    cursor: pointer;
    transition: all 0.5s cubic-bezier(0.68, -0.55, 0.27, 1.55);
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15);
    overflow: hidden;
}

input:checked+.toggle {
    background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
}

.sun {
    position: absolute;
    top: 6px;
    left: 6px;
    width: 16px;
    height: 16px;
    background: #ffd000;
    border-radius: 50%;
    box-shadow: 0 0 10px #ffd000, 0 0 20px rgba(255, 208, 0, 0.5);
    transition: all 0.55s ease;
}

/* .sun .ray {
    position: absolute;
    width: 2px;
    height: 6px;
    background: #ffd000;
    left: 50%;
    top: -8px;
    transform-origin: center 14px;
    border-radius: 1px;
} */
.sun .ray:nth-child(1) {
    transform: translateX(-50%) rotate(0deg);
}

.sun .ray:nth-child(2) {
    transform: translateX(-50%) rotate(45deg);
}

.sun .ray:nth-child(3) {
    transform: translateX(-50%) rotate(90deg);
}

.sun .ray:nth-child(4) {
    transform: translateX(-50%) rotate(135deg);
}

.sun .ray:nth-child(5) {
    transform: translateX(-50%) rotate(180deg);
}

.sun .ray:nth-child(6) {
    transform: translateX(-50%) rotate(225deg);
}

.sun .ray:nth-child(7) {
    transform: translateX(-50%) rotate(270deg);
}

.sun .ray:nth-child(8) {
    transform: translateX(-50%) rotate(315deg);
}

.cloud {
    position: absolute;
    width: 22px;
    height: 8px;
    background: white;
    border-radius: 100px;
    bottom: 6px;
    right: 8px;
    opacity: 0.9;
    transition: all 0.5s ease;
}

.cloud::before,
.cloud::after {
    content: '';
    position: absolute;
    background: white;
    border-radius: 50%;
}

.cloud::before {
    width: 10px;
    height: 10px;
    top: -5px;
    left: 3px;
}

.cloud::after {
    width: 14px;
    height: 12px;
    top: -6px;
    right: 5px;
}

.moon {
    position: absolute;
    width: 16px;
    height: 16px;
    top: 6px;
    right: 6px;
    background: #f1f5f9;
    border-radius: 50%;
    box-shadow: 0 0 10px #f1f5f9;
    transform: translateX(35px);
    opacity: 0;
    transition: all 0.55s ease;
}

.moon .crater {
    position: absolute;
    background: #cbd5e1;
    border-radius: 50%;
}

.crater-1 {
    width: 4px;
    height: 4px;
    top: 4px;
    left: 3px;
}

.crater-2 {
    width: 3px;
    height: 3px;
    top: 9px;
    right: 4px;
}

.stars {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: -20px;
}

.star {
    position: absolute;
    background: white;
    border-radius: 50%;
    transform: translateX(35px);
    opacity: 0;
    transition: all 0.5s ease;
}

.star:nth-child(1) {
    width: 2px;
    height: 2px;
    top: 7px;
    right: 30px;
    transition-delay: 0.1s;
}

.star:nth-child(2) {
    width: 1.5px;
    height: 1.5px;
    top: 16px;
    right: 22px;
    transition-delay: 0.2s;
}

.star:nth-child(3) {
    width: 2px;
    height: 2px;
    top: 10px;
    right: 12px;
    transition-delay: 0.3s;
}

.star:nth-child(4) {
    width: 1px;
    height: 1px;
    top: 18px;
    right: 5px;
    transition-delay: 0.4s;
}

/* 动画触发 */
input:checked+.toggle .sun {
    transform: translateX(-35px);
    opacity: 0;
}

input:checked+.toggle .cloud {
    transform: translateX(-40px);
    opacity: 0;
}

input:checked+.toggle .moon {
    transform: translateX(0);
    opacity: 1;
}

input:checked+.toggle .star {
    transform: translateX(0);
    opacity: 1;
}
</style>