<template>
    <!-- 直接加 class：aside -->
    <div class="main-layout">

        <div class="sidebar aside">
            <h3 style="width: 100%; text-align: center; margin-top: 10px;">健康管理系统</h3>
            
        </div>

        <div class="content-wrap">
            <!-- 顶部 直接加 class：header -->
            <div class="header">
                <div class="icon" style="width: 16%; height: 100%; text-wrap: nowrap;">
                    <el-icon size="22px">
                        <Eleme />
                    </el-icon>
                    <span style="font-size: 180%;">健康管理系统</span>

                </div>

            </div>

            <!-- 内容区 直接加 class：main -->
            <div class="content main">
                <router-view />
            </div>
        </div>
    </div>
</template>

<style scoped>
* {
    transition: all 0.5s ease;
    /* border: 1px solid red; */
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
    justify-content: center;
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
</style>