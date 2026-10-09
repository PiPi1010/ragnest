import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('../layout/MainLayout.vue'),
      redirect: '/knowledge-bases',
      children: [
        {
          path: 'knowledge-bases',
          name: 'knowledge-bases',
          component: () => import('../views/KnowledgeBase.vue'),
          meta: { requiresAuth: true, title: '知识库' }
        },
        {
          path: 'documents',
          name: 'documents',
          component: () => import('../views/Document.vue'),
          meta: { requiresAuth: true, title: '文档' }
        },
        {
          path: 'chat',
          name: 'chat',
          component: () => import('../views/Chat.vue'),
          meta: { requiresAuth: true, title: '对话' }
        }
      ]
    }
  ]
})

// 路由守卫：未登录跳转登录页
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('ragnest_token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
