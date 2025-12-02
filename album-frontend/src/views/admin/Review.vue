<template>
  <div class="review-container">
    <h2>动态审查 - 用户公开相册</h2>

    <el-row :gutter="20">
      <el-col v-for="user in users" :key="user.id" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="user-card" shadow="hover">
          <div class="user-name">{{ user.name }} 的公开相册</div>

          <!-- 遍历用户的所有公开相册 -->
          <div v-for="album in user.albums" :key="album.id" class="album-item">
            <img :src="album.cover" class="album-cover" />
            <el-button
              type="primary"
              size="small"
              @click="reviewAlbum(user.id, user.name,album.id)"
              style="margin-top: 5px;"
            >
              审查
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const users = ref([])

// 获取用户及公开相册列表
const loadUsers = async () => {
  try {
    const res = await axios.get('/api/admin/public-albums')
    if (res.data.code === 200 && Array.isArray(res.data.data)) {
      // 遍历用户列表和相册列表，补全封面 URL
      users.value = res.data.data.map(user => {
        return {
          ...user,
          albums: Array.isArray(user.albums)
            ? user.albums.map(album => ({
                ...album,
                cover: fixUrl(album.cover)
              }))
            : []
        }
      })
    } else {
      users.value = []
    }
  } catch (err) {
    console.error(err)
    users.value = []
  }
}

// URL 补全函数
const fixUrl = (url) => {
  if (!url) return ''          // 空值返回空字符串
  if (/^https?:\/\//.test(url)) return url  // 已经是完整 URL
  return `http://localhost:8080${url}`      // 补全本地 URL
}


// const reviewAlbum = (userId, albumId) => {
//   router.push({ name: 'adminReviewDetail', params: { userId, albumId } })
// }
const reviewAlbum = (userId, userName, albumId) => {
  console.log('Navigating to review album:', userId, userName, albumId)
  router.push({
    name: 'adminReviewDetail',
    params: { userId, userName, albumId }
  })
}


// 页面加载时请求
onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.review-container { padding: 20px; }
.user-card { text-align: center; margin-bottom: 20px; }
.user-name { font-weight: bold; margin-bottom: 10px; }
.album-item { margin-bottom: 10px; }
.album-cover { width: 100%; height: 150px; object-fit: cover; margin-bottom: 5px; }
</style>
