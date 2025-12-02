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
const userId = JSON.parse(localStorage.getItem('user') || '{}').id;

// ------- 数据 -------
const friendName = ref('加载中...')
const albumName = ref('加载中...')
const images = ref([])

const token = localStorage.getItem('token')

// ------- 获取好友信息 -------
const loadFriendInfo = async () => {
  try {
    if (!friendId) return

    const res = await axios.get(`/api/friend/${friendId}`)
    if (res.data.code === 200 && res.data.data) {
      friendName.value = res.data.data.name
    } else {
      ElMessage.error(res.data.msg || '好友信息加载失败')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('好友信息加载失败')
  }
}

// ------- 获取相册信息 -------
const loadAlbumInfo = async () => {
  try {
    if (!albumId) return

    const res = await axios.get(`/api/album/${albumId}`)
    if (res.data.code === 200 && res.data.data) {
      albumName.value = res.data.data.name
    } else {
      ElMessage.error(res.data.msg || '相册信息加载失败')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('相册信息加载失败')
  }
}

// URL 补全函数
const fixUrl = (url) => {
  if (!url) return ''
  if (/^https?:\/\//.test(url)) return url   // 已经是完整链接
  return `http://localhost:8080${url}`       // 自动补全
}

// ------- 获取相册图片 -------
const loadAlbumImages = async () => {
  try {
    if (!albumId) return

    const res = await axios.get(`/api/album/${albumId}/images`)

    if (res.data.code === 200 && Array.isArray(res.data.data)) {
      // 这里统一处理 URL
      images.value = res.data.data.map(img => ({
        ...img,
        url: fixUrl(img.url)
      }))
    } else {
      images.value = []
      ElMessage.info(res.data.msg || '暂无图片')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('相册图片加载失败')
  }
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
  if (!img || !userId) return

  img.liked = !img.liked

  try {
    await axios.post(`/api/image/${imageId}/like`, {
      user_id: userId,    // 带上用户ID
      liked: img.liked    // 当前点赞状态
    })
  } catch (err) {
    console.error(err)
    ElMessage.error('操作失败')
    // 点赞失败则回退状态
    img.liked = !img.liked
  }
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
  if (!text || !userId) return

  const newObj = {
    id: Date.now(),
    content: text,
    user: { id: userId, nickname: '我', avatar: '' } // 可根据实际情况更新昵称和头像
  }

  // 前端立即更新界面
  currentImage.value.comments.push(newObj)
  currentImageComments.value.push(newObj)
  newComment.value = ''

  await nextTick()
  scrollToBottom()

  try {
    await axios.post(`/api/image/${currentImage.value.id}/comment`, {
      user_id: userId,  // 带上用户ID
      content: text
    })
  } catch (err) {
    console.error(err)
    ElMessage.error('评论提交失败')
    // 可以选择回退评论
    currentImage.value.comments.pop()
    currentImageComments.value.pop()
  }
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
