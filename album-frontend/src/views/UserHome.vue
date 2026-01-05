<template>
  <el-container style="height: 100vh;width: 100vw;">
    <!-- 顶部栏 -->
    <el-header class="header">
      <div class="title">电子相册</div>
      <div class="header-right">
        <el-avatar :size="35" :src="user.avatar" />
        <span class="nickname">{{ user.nickname }}</span>
      </div>
    </el-header>

    <!-- 左右布局 -->
    <el-container style="flex: 1;">
      <!-- 左侧菜单 -->
      <el-aside width="200px" class="aside">
        <el-menu :default-active="activeMenu" @select="handleMenuSelect" class="el-menu-vertical">
          <el-menu-item index="album">
            <el-icon><Picture /></el-icon>
            <span>相册</span>
          </el-menu-item>
          <el-menu-item index="friends">
            <el-icon><User /></el-icon>
            <span>好友</span>
          </el-menu-item>
          <!-- <el-menu-item index="notifications">
            <el-icon><Bell /></el-icon>
            <span>通知</span>
          </el-menu-item> -->
          <el-menu-item index="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主体内容 -->
      <el-main class="main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Picture, User, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const activeMenu = ref('album')

// 登录用户信息
const user = ref({
  nickname: '',
  avatar: ''
})
// URL 补全函数（可以复用之前的）
const fixUrl = (url) => {
  if (!url) return ''
  if (/^https?:\/\//.test(url)) return url
  return `http://localhost:8080${url}`
}

onMounted(() => {
  const savedUser = localStorage.getItem('user')
  if (savedUser) {
    try {
      const parsed = JSON.parse(savedUser)
      user.value.nickname = parsed.nickname || '用户昵称'
      user.value.avatar = fixUrl(parsed.avatar || '')
    } catch (e) {
      console.error('解析用户信息失败', e)
    }
  }
})

const handleMenuSelect = (index) => {
  if (index === 'logout') {
    localStorage.removeItem('user') // 退出登录清除
    router.push('/login')
    return
  }
  activeMenu.value = index
  router.push(`/user/${index}`)
}
</script>


<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #409EFF;
  color: white;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.header-right {
  display: flex;
  align-items: center;
}

.nickname {
  margin-left: 10px;
}

.aside {
  background-color: #f5f5f5;
}

.main {
  padding: 20px;
  background-color: #fff;
  overflow-y: auto;
}
</style>
