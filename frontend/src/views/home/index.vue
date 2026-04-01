<template>
    <div class="home-page" @mousemove="onMouseMove" @mouseleave="onMouseLeave" ref="pageRef">
        <!-- 3D 科技网格背景 -->
        <div class="grid-3d-bg" :style="gridStyle"></div>

        <!-- 粒子 Canvas -->
        <canvas ref="canvasRef" class="particle-canvas"></canvas>

        <!-- 标题区域 -->
        <div class="title-box" :style="titleStyle">
            <h1 class="main-title">健康管理系统</h1>
            <p class="slogan">Health Management System</p>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, reactive } from 'vue'

const canvasRef = ref(null)
const pageRef = ref(null)
let animationId = null
let ctx = null
let width = 0
let height = 0

// 粒子数组
let particles = []
// 鼠标位置（用于3D视差）
let mouse = { x: 0, y: 0, active: false }
// 目标鼠标位置（平滑过渡）
let targetMouse = { x: 0, y: 0 }
// 全局时间变量
let globalTime = 0

// 3D 网格样式
const gridStyle = reactive({
    transform: 'perspective(900px) rotateX(100deg) translateZ(0px) translateY(0px)'
})

// 标题样式
const titleStyle = reactive({
    transform: 'translate(0px, 0px)'
})

// ==================== 粒子参数配置 ====================
const PARTICLE_SIZE = 4
const PARTICLE_SPACING = 20

/**
 * 粒子类 - 增强海浪效果
 */
class Particle {
    constructor(x, y, phase) {
        this.ox = x          // 原始X
        this.oy = y          // 原始Y
        this.x = x
        this.y = y
        this.phase = phase   // 随机相位
        this.waveHeight = 0  // 当前波浪高度
    }

    /**
     * 更新海浪位置 - 多层波浪叠加，产生明显起伏
     */
    update(t, mouseX, mouseY, mouseActive) {
        // 基础波浪参数 - 增强幅度和频率
        const time = t * 1.5

        // 第一层：大浪（低频高幅）- 主要波浪
        const wave1 = Math.sin(this.ox * 0.008 + time) * 1.2
        const wave1b = Math.cos(this.oy * 0.008 + time * 0.9) * 1.2
        const mainWave = (wave1 + wave1b) * 1.5

        // 第二层：中浪（中频）
        const wave2 = Math.sin(this.ox * 0.018 - time * 1.3) * 0.9
        const wave2b = Math.cos(this.oy * 0.016 + time * 1.4) * 0.9
        const midWave = (wave2 + wave2b) * 1.2

        // 第三层：波纹细节（高频）
        const wave3 = Math.sin(this.ox * 0.035 + time * 2.2) * 1.0
        const wave3b = Math.cos(this.oy * 0.032 - time * 2.0) * 1.0
        const detailWave = wave3 + wave3b

        // 第四层：交叉波浪（产生网格状海浪）
        const crossWave = Math.sin((this.ox * 0.012 + this.oy * 0.008) * 1.5 + time * 1.7) * 0.9

        // 组合海浪高度（增强幅度）
        let waveHeight = mainWave + midWave + detailWave + crossWave

        // 添加动态衰减因子，让波浪更有节奏感
        const beat = Math.sin(t * 0.6) * 0.4
        waveHeight = waveHeight * (1 + beat)

        // 鼠标交互：产生扩散涟漪（增强交互感）
        if (mouseActive && mouseX > 0 && mouseX < width && mouseY > 0 && mouseY < height) {
            const dx = this.x - mouseX
            const dy = this.y - mouseY
            const dist = Math.hypot(dx, dy)
            const radius = 350

            if (dist < radius) {
                // 涟漪波形 - 明显的水波扩散效果
                const rippleStrength = Math.cos(dist * 0.04 - t * 12) * Math.pow(1 - dist / radius, 1.5)
                waveHeight += rippleStrength * 2
            }
        }

        // 存储波浪高度用于颜色计算
        this.waveHeight = waveHeight

        // 根据波浪高度影响X/Y位置（产生横向流动感）
        const flowStrength = 1.2
        const flowX = Math.sin(this.oy * 0.012 + t * 1.4) * flowStrength * (waveHeight * 0.3)
        const flowY = Math.cos(this.ox * 0.012 + t * 1.3) * flowStrength * (waveHeight * 0.3)

        // 更新位置 - 波浪幅度更明显
        this.x = this.ox + flowX + (waveHeight * 0.5)
        this.y = this.oy + flowY + (waveHeight * 0.4)

        // 添加Y轴的上下浮动（增强立体感）
        this.y += waveHeight * 0.8
    }

    /**
     * 获取海浪颜色 - 随波浪高度变化更明显
     */
    getColor(t, waveHeight) {
        // 根据波浪高度改变颜色（浪高时更亮，浪低时更深）
        const normalizedHeight = (waveHeight + 2) / 4 // 范围 -2 到 2 映射到 0-1

        // 浪尖更亮偏青，浪谷更深偏蓝
        const hue = 195 + normalizedHeight * 25  // 195-220
        const saturation = 50 + normalizedHeight * 20  // 50-70%
        const lightness = 48 + normalizedHeight * 25  // 48-73%

        // 添加时间微调产生呼吸感
        const breath = Math.sin(t * 0.4) * 3
        return `hsl(${hue + breath * 0.5}, ${saturation + breath * 0.3}%, ${lightness + breath}%)`
    }

    draw(ctx, t) {
        // 根据波浪高度改变粒子大小（浪尖粒子更大更亮）
        const sizeScale = 0.7 + this.waveHeight * 0.25
        const finalSize = PARTICLE_SIZE * Math.max(0.5, Math.min(1.5, sizeScale))

        const color = this.getColor(t, this.waveHeight)
        ctx.fillStyle = color

        // 浪尖粒子添加光晕效果
        if (this.waveHeight > 1) {
            ctx.shadowBlur = 8
            ctx.shadowColor = 'rgba(100, 180, 255, 0.6)'
        } else {
            ctx.shadowBlur = 3
            ctx.shadowColor = color
        }

        ctx.beginPath()
        ctx.arc(this.x, this.y, finalSize, 0, Math.PI * 2)
        ctx.fill()
    }
}

// ==================== 初始化粒子网格 ====================
function initParticles() {
    particles = []
    const step = PARTICLE_SPACING
    for (let y = step / 2; y < height; y += step) {
        for (let x = step / 2; x < width; x += step) {
            // 随机相位使波浪更自然
            const phase = Math.random() * Math.PI * 2
            particles.push(new Particle(x, y, phase))
        }
    }
}

// ==================== Canvas 绘制主循环 ====================
function draw() {
    if (!ctx) return

    globalTime += 0.016

    // 平滑鼠标移动（用于3D视差）
    targetMouse.x += (mouse.x - targetMouse.x) * 0.08
    targetMouse.y += (mouse.y - targetMouse.y) * 0.08

    // 更新3D网格视差效果
    const gridOffsetX = (targetMouse.x - 0.5) * 40
    const gridOffsetY = (targetMouse.y - 0.5) * 30
    gridStyle.transform = `perspective(900px) rotateX(55deg) translateZ(${gridOffsetY}px) translateY(${gridOffsetX * 0.3}px)`

    // 更新标题视差
    titleStyle.transform = `translate(${(targetMouse.x - 0.5) * 25}px, ${(targetMouse.y - 0.5) * 20}px)`

    // 清空画布并添加半透明光晕
    ctx.clearRect(0, 0, width, height)

    // 添加海洋氛围光晕
    const gradient = ctx.createLinearGradient(0, 0, width, height)
    gradient.addColorStop(0, 'rgba(10, 30, 55, 0.2)')
    gradient.addColorStop(1, 'rgba(5, 15, 35, 0.3)')
    ctx.fillStyle = gradient
    ctx.fillRect(0, 0, width, height)

    // 更新所有粒子并绘制
    const mouseActive = mouse.active && mouse.x > 0 && mouse.x < width && mouse.y > 0 && mouse.y < height
    const mouseCanvasX = mouse.x * width
    const mouseCanvasY = mouse.y * height

    for (let i = 0; i < particles.length; i++) {
        const p = particles[i]
        p.update(globalTime, mouseCanvasX, mouseCanvasY, mouseActive)
        p.draw(ctx, globalTime)
    }

    ctx.shadowBlur = 0

    animationId = requestAnimationFrame(draw)
}

// ==================== 鼠标事件处理 ====================
const onMouseMove = (e) => {
    const rect = pageRef.value?.getBoundingClientRect()
    if (rect) {
        // 归一化鼠标坐标 (0-1)
        mouse.x = Math.min(Math.max((e.clientX - rect.left) / rect.width, 0), 1)
        mouse.y = Math.min(Math.max((e.clientY - rect.top) / rect.height, 0), 1)
        mouse.active = true
    }
}

const onMouseLeave = () => {
    mouse.active = false
    mouse.x = 0.5
    mouse.y = 0.5
}

// ==================== 窗口自适应 ====================
const handleResize = () => {
    if (!canvasRef.value) return

    width = window.innerWidth
    height = window.innerHeight

    canvasRef.value.width = width
    canvasRef.value.height = height

    initParticles()

    ctx = canvasRef.value.getContext('2d')
}

// ==================== 生命周期 ====================
onMounted(() => {
    if (!canvasRef.value) return

    width = window.innerWidth
    height = window.innerHeight

    canvasRef.value.width = width
    canvasRef.value.height = height

    ctx = canvasRef.value.getContext('2d')

    initParticles()

    draw()

    window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
    if (animationId) {
        cancelAnimationFrame(animationId)
    }
    window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.home-page {
    position: relative;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at 20% 30%, #0a1425, #03050f);
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: default;
}

/* 3D 透视科技网格背景 - 海浪主题增强 */
.grid-3d-bg {
    position: absolute;
    inset: 0;
    background-image:
        linear-gradient(rgba(65, 156, 235, 0.15) 1px, transparent 1px),
        linear-gradient(90deg, rgba(22, 131, 227, 0.15) 1px, transparent 1px);
    background-size: 60px 60px;
    transform-origin: center bottom 0px;
    z-index: 0;
    opacity: 0.6;
    transition: transform 0.1s ease-out;
    will-change: transform;
    animation: gridPulse 4s ease-in-out infinite;
}

@keyframes gridPulse {

    0%,
    100% {
        opacity: 0.5;
    }

    50% {
        opacity: 0.7;
    }
}

/* 添加海浪光晕效果 */
.grid-3d-bg::before {
    content: '';
    position: absolute;
    inset: 0;
    background: radial-gradient(ellipse at 50% 40%, rgba(90, 165, 239, 0.69) 0%, transparent 70%);
    pointer-events: none;
    animation: oceanBreath 6s ease-in-out infinite;
}

@keyframes oceanBreath {

    0%,
    100% {
        opacity: 0.4;
        transform: scale(1);
    }

    50% {
        opacity: 0.8;
        transform: scale(1.05);
    }
}

.grid-3d-bg::after {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(45deg,
            transparent,
            transparent 50px,
            rgba(70, 140, 210, 0.05) 50px,
            rgba(70, 140, 210, 0.05) 100px);
    pointer-events: none;
}

.particle-canvas {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 1;
    display: block;
}

.title-box {
    position: relative;
    z-index: 2;
    text-align: center;
    user-select: none;
    backdrop-filter: blur(12px);
    padding: 28px 64px;
    border-radius: 56px;
    background: rgba(8, 18, 35, 0.45);
    border: 1px solid rgba(70, 150, 230, 0.3);
    box-shadow: 0 20px 50px rgba(0, 0, 0, 0.25);
    transition: transform 0.15s ease-out;
    will-change: transform;
    animation: titleFloat 4s ease-in-out infinite;
}

@keyframes titleFloat {

    0%,
    100% {
        transform: translateY(0px);
    }

    50% {
        transform: translateY(-8px);
    }
}

.main-title {
    font-size: 74px;
    font-weight: 800;
    margin: 0;
    letter-spacing: 8px;
    background: linear-gradient(135deg, #c8e4ff, #7cb0ff);
    background-clip: text;
    -webkit-background-clip: text;
    color: transparent;
    text-shadow: none;
    animation: titleWaveGlow 3s ease-in-out infinite;
}

@keyframes titleWaveGlow {

    0%,
    100% {
        filter: drop-shadow(0 0 10px rgba(70, 140, 220, 0.5));
    }

    50% {
        filter: drop-shadow(0 0 25px rgba(100, 170, 250, 0.8));
    }
}

.slogan {
    font-size: 18px;
    color: rgba(170, 200, 240, 0.9);
    margin-top: 18px;
    letter-spacing: 3px;
    font-weight: 400;
    text-transform: uppercase;
    backdrop-filter: blur(2px);
}

@media (max-width: 768px) {
    .main-title {
        font-size: 44px;
        letter-spacing: 4px;
    }

    .slogan {
        font-size: 14px;
    }

    .title-box {
        padding: 20px 40px;
    }
}
</style>