import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/Index.vue')
  },
  {
    path: '/user/info',
    component: () => import('@/views/user/Info.vue')
  },
  {
    path: '/health/record',
    component: () => import('@/views/health/Record.vue')
  },
  {
    path: '/health/report',
    component: () => import('@/views/health/Report.vue')
  },
  {
    path: '/diet',
    component: () => import('@/views/diet/Index.vue')
  },
  {
    path: '/sport',
    component: () => import('@/views/sport/Index.vue')
  },
  {
    path: '/sleep',
    component: () => import('@/views/sleep/Index.vue')
  },
  {
    path: '/system/setting',
    component: () => import('@/views/system/Setting.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
