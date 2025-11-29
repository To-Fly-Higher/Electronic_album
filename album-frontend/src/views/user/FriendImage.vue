<template>
  <div class="album-container">
  <!-- 面包屑导航 -->
  <el-breadcrumb separator="/">
    <el-breadcrumb-item style="cursor: pointer;" @click="goFriendsList">
      好友列表
    </el-breadcrumb-item>

    <el-breadcrumb-item style="cursor: pointer;" @click="goFriendAlbums">
      {{ friendName }}的相册
    </el-breadcrumb-item>

    <el-breadcrumb-item>
      {{ albumName }}
    </el-breadcrumb-item>
  </el-breadcrumb>

    <el-row :gutter="20" style="margin-top: 10px;">
      <!-- 图片卡片 -->
      <el-col
        v-for="image in images"
        :key="image.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card class="image-card" :body-style="{ padding: '10px' }" shadow="hover">
          <!-- 图片 -->
          <img :src="image.url" class="image-cover" />

          <!-- 图片操作按钮 -->
          <div class="image-actions">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-thumb"
              @click="toggleLike(image.id)"
            >
              {{ image.liked ? '已赞' : '点赞' }}
            </el-button>
            <el-button
              size="mini"
              type="success"
              icon="el-icon-chat-dot-round"
              @click="openComment(image)"
            >
              评论
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 评论弹窗 -->
    <el-dialog
      title="评论"
      v-model="commentDialogVisible"
      width="400px"
      @open="scrollToBottom"
    >
      <div class="comment-list" ref="commentList">
        <div v-for="(cmt, index) in currentImageComments" :key="index" class="comment-item">
          <strong>{{ cmt.user.nickname }}:</strong> {{ cmt.content }}
        </div>
        <div v-if="currentImageComments.length === 0" class="no-comments">
          暂无评论
        </div>
      </div>

      <el-input
        type="textarea"
        v-model="newComment"
        placeholder="输入评论..."
        rows="3"
      ></el-input>

      <template #footer>
        <el-button @click="cancelComment">取消</el-button>
        <el-button type="primary" @click="submitComment">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'  

const route = useRoute()
const router = useRouter()

const friendId = route.params.friendId
const albumId = route.params.albumId

// ------- 数据 -------
const friendName = ref('加载中...')
const albumName = ref('加载中...')
const images = ref([])

const token = localStorage.getItem('token')

// ------- 获取好友信息 -------
const loadFriendInfo = async () => {
  const res = await axios.get(`/api/friend/${friendId}`, {
    headers: { Authorization: `Bearer ${token}` }
  })
  friendName.value = res.data.data.name
}

// ------- 获取相册信息 -------
const loadAlbumInfo = async () => {
  const res = await axios.get(`/api/album/${albumId}`, {
    headers: { Authorization: `Bearer ${token}` }
  })
  albumName.value = res.data.data.name
}

// ------- 获取相册图片 -------
const loadAlbumImages = async () => {
  const res = await axios.get(`/api/album/${albumId}/images`, {
    headers: { Authorization: `Bearer ${token}` }
  })
  images.value = res.data.data
}

// 初始化加载
onMounted(async () => {
  await Promise.all([
    loadFriendInfo(),
    loadAlbumInfo(),
    loadAlbumImages()
  ])
})


// ------- 点赞 -------
const toggleLike = async (imageId) => {
  const img = images.value.find(i => i.id === imageId)
  if (!img) return

  img.liked = !img.liked

  await axios.post(`/api/image/${imageId}/like`, 
    { liked: img.liked },
    { headers: { Authorization: `Bearer ${token}` } }
  )
}


// ------- 评论 -------
const commentDialogVisible = ref(false)
const currentImage = ref(null)
const currentImageComments = ref([])
const newComment = ref('')
const commentList = ref(null)

const openComment = (image) => {
  currentImage.value = image
  // 直接使用后端返回的数据
  currentImageComments.value = [...image.comments]
  newComment.value = ''
  commentDialogVisible.value = true
}

const cancelComment = () => {
  commentDialogVisible.value = false
}


// ------- 评论 -------
const submitComment = async () => {
  const text = newComment.value.trim()
  if (!text) return

  const newObj = {
    id: Date.now(),
    content: text,
    user: { id: 0, nickname: '我', avatar: '' }
  }

  currentImage.value.comments.push(newObj)
  currentImageComments.value.push(newObj)
  newComment.value = ''

  await nextTick()
  scrollToBottom()

  await axios.post(`/api/image/${currentImage.value.id}/comment`, 
    { content: text },
    { headers: { Authorization: `Bearer ${token}` } }
  )
}




const scrollToBottom = () => {
  if (commentList.value) {
    commentList.value.scrollTop = commentList.value.scrollHeight
  }
}


// ------- 面包屑 -------
const goFriendsList = () => {
  router.push('/user/friends')
}

const goFriendAlbums = () => {
  router.push(`/user/friend-album/${friendId}`)
}
</script>


<style scoped>
.album-container {
  padding: 20px;
}

.image-card {
  text-align: center;
  margin-bottom: 20px;
}

.image-cover {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.image-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 5px;
}

.comment-list {
  max-height: 200px;
  overflow-y: auto;
  margin-bottom: 10px;
  border: 1px solid #eee;
  padding: 5px;
  border-radius: 4px;
}

.comment-item {
  margin-bottom: 5px;
}

.no-comments {
  color: #999;
  text-align: center;
}
</style>
