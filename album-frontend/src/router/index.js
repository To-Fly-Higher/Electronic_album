import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import AdminHome from '../views/AdminHome.vue'
import UserHome from '../views/UserHome.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/admin', component: AdminHome },
  {
    path: '/user',
    component: UserHome,
    children: [
      { path: 'album', name: 'album', component: () => import('../views/user/Album.vue') },
      { path: 'album/:id', name: 'albumDetail', component: () => import('../views/user/AlbumDetail.vue'), props: true },
      { path: 'friends', name: 'friends', component: () => import('../views/user/Friends.vue') },
      { path: 'notifications', name: 'notifications', component: () => import('../views/user/Notifications.vue') },
      { path: 'friend-album/:friendId', name: 'friendAlbum', component: () => import('../views/user/FriendAlbum.vue'), props: true }
    ]
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
