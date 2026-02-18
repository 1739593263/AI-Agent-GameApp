import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '游戏大师AI问答平台' }
  },
  {
    path: '/game-master',
    name: 'GameMaster',
    component: () => import('../views/GameMasterChat.vue'),
    meta: { title: 'AI 游戏大师' }
  },
  {
    path: '/super-agent',
    name: 'SuperAgent',
    component: () => import('../views/SuperAgentChat.vue'),
    meta: { title: 'AI 超级智能体' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title || '游戏大师AI问答平台'
  next()
})

export default router
