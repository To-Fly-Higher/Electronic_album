<template>
  <div class="album-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click="goFriendsList" style="cursor: pointer;">好友列表</el-breadcrumb-item>
      <el-breadcrumb-item>{{ friendName }} 的公开相册</el-breadcrumb-item>
    </el-breadcrumb>

    <el-row :gutter="20" style="margin-top: 10px;">
      <!-- 好友公开相册卡片 -->
      <el-col
        v-for="album in albums"
        :key="album.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card class="album-card" shadow="hover" @click="openAlbum(album.id)">
          <img :src="album.cover" class="album-cover" />
          <div class="album-name">{{ album.name }}</div>
        </el-card>
      </el-col>

      <!-- 暂无相册 -->
      <el-col v-if="albums.length === 0" :xs="24">
        <div class="no-albums">该好友暂无公开相册</div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const friendId = route.params.friendId

const friendName = ref('好友')
const albums = ref([])

// 拉取好友公开相册
const loadAlbums = async () => {
  try {
    if (!friendId) return

    const res = await axios.get(`/api/friend/${friendId}/albums`)

    if (res.data.code === 200 && Array.isArray(res.data.data)) {
      albums.value = res.data.data.map(a => ({
        ...a,
        cover: fixUrl(a.cover) || 'https://picsum.photos/300/200?random=' + a.id
      }))
      console.log(albums.value)
      if (albums.value.length > 0) friendName.value = albums.value[0].owner_name || '好友'
    } else {
      albums.value = []
      ElMessage.info(res.data.msg || '暂无公开相册')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('公开相册加载失败')
  }
}
// URL 补全函数（可以复用之前的）
const fixUrl = (url) => {
  if (!url) return ''
  if (/^https?:\/\//.test(url)) return url
  return `http://localhost:8080${url}`
}


// 点击进入相册详情页

const openAlbum = (albumId) => {
  router.push(`/user/friend-album/${friendId}/${albumId}`)
}

// 返回好友列表
const goFriendsList = () => {
  router.push('/user/friends')
}

onMounted(() => {
  loadAlbums()
})
</script>

<style scoped>
.album-container {
  padding: 20px;
}

.album-card {
  text-align: center;
  margin-bottom: 20px;
  cursor: pointer;
}

.album-cover {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.album-name {
  margin-top: 8px;
  font-weight: bold;
  font-size: 16px;
  text-align: center;
}

.no-albums {
  text-align: center;
  color: #999;
  margin-top: 20px;
  font-size: 14px;
}
</style>
