import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ViaView from '../views/ViaView.vue'
import TipoViaView from '@/views/TipoViaView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/vias',
      name: 'ViaView',
      component: ViaView
    },
    {
      path: '/tipo-vias',
      name: 'TipoViaView',
      component: TipoViaView
    },
    // {
    //   path: '/about',
    //   name: 'about',
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
})

export default router
