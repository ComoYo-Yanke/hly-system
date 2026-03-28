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
    ]
    
  },
  {
    path: '/user/info',
    
  },
  {
    path: '/health/record',
    
  },
  {
    path: '/health/report',
  },
  {
    path: '/system/setting',
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
