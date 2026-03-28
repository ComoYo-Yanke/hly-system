import { createRouter, createWebHistory } from 'vue-router'
import Main from '@/layout/main.vue'
import { componentSizes } from 'element-plus'

const routes = [
  {
    path: '/',
    redirect: '/home'  // ← 加这个
  },
  {
    path: '/home',
    name: 'home',
    component: Main,
    children:[
      {path: '',component:() => import('@/views/home/index.vue')},
      {path: '/user/info', component:() => import('@/views/user/index.vue')},
      {path: '/health/event',component:() => import('@/views/health/event.vue')},
      {path: '/health/remind',component:() => import('@/views/health/remind.vue')},
      {path: '/system/setting',}
    ]
    
  },
  
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
