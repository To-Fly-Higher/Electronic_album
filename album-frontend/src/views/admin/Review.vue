<template>
  <div class="review-container">
    <h2>动态审查 - 用户公开相册</h2>

    <el-row :gutter="20">
      <el-col v-for="user in users" :key="user.id" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="user-card" shadow="hover">
          <div class="user-name">{{ user.name }} 的公开相册</div>

          <!-- 展示用户的公开相册封面 -->
          <img :src="user.album.cover" class="album-cover" />

          <!-- 审查按钮 -->
          <el-button type="primary" size="small" @click="reviewAlbum(user.id)" style="margin-top:10px;">
            审查
          </el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 模拟用户及公开相册数据（每个用户只有一个公开相册）
const users = ref([
  { id: 1, name: 'Alice', album: { id: 101, cover: 'https://picsum.photos/300/200?random=1' } },
  { id: 2, name: 'Bob', album: { id: 102, cover: 'https://picsum.photos/300/200?random=2' } },
  { id: 3, name: 'Charlie', album: { id: 103, cover: 'https://picsum.photos/300/200?random=3' } },
])

const reviewAlbum = (userId) => {
  router.push({ name: 'adminReviewDetail', params: { userId } })
}
</script>

<style scoped>
.review-container { padding: 20px; }
.user-card { text-align: center; margin-bottom: 20px; }
.user-name { font-weight: bold; margin-bottom: 10px; }
.album-cover { width: 100%; height: 150px; object-fit: cover; margin-bottom: 5px; }
</style>
