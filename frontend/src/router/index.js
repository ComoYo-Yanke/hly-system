import { createRouter, createWebHistory } from 'vue-router'
import Main from '@/layout/main.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false } // 不需要登录
  },
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'home',
    component: Main,
    meta: { requiresAuth: true }, // 需要登录
    children: [
      { 
        path: '', 
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { requiresAuth: true }
      },
      { 
        path: '/user/info', 
        name: 'UserInfo',
        component: () => import('@/views/user/index.vue'),
        meta: { requiresAuth: true }
      },
      { 
        path: '/health/event', 
        name: 'HealthEvent',
        component: () => import('@/views/health/event.vue'),
        meta: { requiresAuth: true }
      },
      { 
        path: '/health/remind', 
        name: 'HealthRemind',
        component: () => import('@/views/health/remind.vue'),
        meta: { requiresAuth: true }
      },
      { 
        path: '/health-statistics', 
        name: 'HealthStatistics',
        component: () => import('@/views/health/statistics.vue'),
        meta: { requiresAuth: true }
      },
      { 
        path: '/health-square', 
        name: 'HealthSquare',
        component: () => import('@/views/health/square.vue'),
        meta: { requiresAuth: true }
      },
      { 
        path: '/system/setting',
        name: 'SystemSetting',
        component: () => import('@/views/system/setting.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  // 404 页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
    meta: { requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 登录验证
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  
  // 如果页面需要登录认证
  if (requiresAuth) {
    if (!token) {
      // 未登录，跳转到登录页，并记录要返回的页面
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      next()
    }
  } else {
    // 不需要登录的页面
    if (to.path === '/login' && token) {
      // 已登录，访问登录页，跳转到首页
      next('/home')
    } else {
      next()
    }
  }
})

export default router