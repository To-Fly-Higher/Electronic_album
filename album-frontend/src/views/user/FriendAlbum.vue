<template>
  <div class="album-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click="goFriendsList" style="cursor: pointer;">好友列表</el-breadcrumb-item>
      <el-breadcrumb-item>{{ albumName }}</el-breadcrumb-item>
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
          <strong>{{ cmt.user }}:</strong> {{ cmt.text }}
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
import { ref, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const albumId = route.params.albumId // 好友相册 ID
const albumName = ref('好友公开相册')

// 模拟相册里的图片数据，每张图片有自己的评论
const images = ref([
  {
    id: 1,
    url: 'https://picsum.photos/300/200?random=1',
    liked: false,
    comments: [
      { user: 'Alice', text: '这张好美！' },
      { user: 'Bob', text: '好喜欢！' }
    ]
  },
  {
    id: 2,
    url: 'https://picsum.photos/300/200?random=2',
    liked: false,
    comments: [
      { user: 'Charlie', text: '校园风景棒棒的！' }
    ]
  },
  {
    id: 3,
    url: 'https://picsum.photos/300/200?random=3',
    liked: false,
    comments: [
      { user: 'Daisy', text: '萌萌的宠物～' },
      { user: 'Eve', text: '太可爱了' }
    ]
  },
])

// 点赞逻辑
const toggleLike = (imageId) => {
  const image = images.value.find(i => i.id === imageId)
  if (image) image.liked = !image.liked
}

// 评论逻辑
const commentDialogVisible = ref(false)
const currentImage = ref(null)
const currentImageComments = ref([])
const newComment = ref('')
const commentList = ref(null)

const openComment = (image) => {
  currentImage.value = image
  currentImageComments.value = [...image.comments]
  newComment.value = ''
  commentDialogVisible.value = true
}

const cancelComment = () => {
  commentDialogVisible.value = false
}

const submitComment = async () => {
  if (!newComment.value.trim()) return
  const commentObj = { user: '我', text: newComment.value.trim() }
  currentImage.value.comments.push(commentObj)
  currentImageComments.value.push(commentObj)
  newComment.value = ''
  await nextTick()
  scrollToBottom()
}

const scrollToBottom = () => {
  if (commentList.value) {
    commentList.value.scrollTop = commentList.value.scrollHeight
  }
}

// 面包屑返回好友列表
const goFriendsList = () => {
  router.push('/user/friends')
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
