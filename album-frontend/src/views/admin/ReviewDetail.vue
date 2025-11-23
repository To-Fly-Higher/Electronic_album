<template>
  <div class="admin-album-detail">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click.native="goBack">动态审查</el-breadcrumb-item>
      <el-breadcrumb-item>{{ user.name }} 的公开相册</el-breadcrumb-item>
    </el-breadcrumb>

    <h2>{{ user.name }} 的公开相册</h2>

    <!-- 删除整个公开相册 -->
    <el-button type="danger" size="small" @click="deleteAlbum" style="margin-bottom: 15px;">
      删除该用户公开相册
    </el-button>

    <!-- 瀑布流展示图片 -->
    <el-row :gutter="15">
      <el-col
        v-for="image in albumImages"
        :key="image.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card class="image-card" :body-style="{ padding: '5px' }">
          <img :src="image.url" class="image-cover" />
          <div class="image-name">{{ image.name }}</div>

          <div class="image-actions">
            <el-button
              size="mini"
              type="success"
              icon="el-icon-chat-dot-round"
              @click="openComments(image)"
            >
              评论
            </el-button>
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="deleteImage(image.id)"
            >
              删除
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
        <div
          v-for="(cmt, index) in currentImageComments"
          :key="index"
          class="comment-item"
        >
          <strong>{{ cmt.user }}:</strong> {{ cmt.text }}
          <el-button
            type="danger"
            size="mini"
            @click="deleteComment(index)"
            style="margin-left: 5px;"
          >
            删除
          </el-button>
        </div>
        <div v-if="currentImageComments.length === 0" class="no-comments">
          暂无评论
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const user = ref({ id: route.params.userId, name: 'Alice' })

// 模拟公开相册图片数据
const albumImages = ref([
  { id: 1, name: '旅游1', url: 'https://picsum.photos/300/200?random=1', comments: [{ user: 'Bob', text: '好美!' }] },
  { id: 2, name: '旅游2', url: 'https://picsum.photos/300/200?random=2', comments: [] },
  { id: 3, name: '旅游3', url: 'https://picsum.photos/300/200?random=3', comments: [{ user: 'Charlie', text: '想去!' }] },
])

// 评论弹窗逻辑
const commentDialogVisible = ref(false)
const currentImage = ref(null)
const currentImageComments = ref([])
const commentList = ref(null)

const openComments = (image) => {
  currentImage.value = image
  currentImageComments.value = [...image.comments]
  commentDialogVisible.value = true
}

const deleteComment = (index) => {
  if (confirm('确认删除该评论吗？')) {
    currentImageComments.value.splice(index, 1)
    const imgIndex = albumImages.value.findIndex(img => img.id === currentImage.value.id)
    if (imgIndex !== -1) albumImages.value[imgIndex].comments = [...currentImageComments.value]
  }
}

const scrollToBottom = () => {
  if (commentList.value) {
    commentList.value.scrollTop = commentList.value.scrollHeight
  }
}

const deleteImage = (imageId) => {
  if (confirm('确认删除该图片吗？')) {
    const index = albumImages.value.findIndex(img => img.id === imageId)
    if (index !== -1) albumImages.value.splice(index, 1)
  }
}

const deleteAlbum = () => {
  if (confirm('确认删除该用户的公开相册吗？')) {
    router.back()
  }
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.admin-album-detail { padding: 20px; }

.image-card { text-align: center; margin-bottom: 15px; }
.image-cover { width: 100%; height: 150px; object-fit: cover; margin-bottom: 5px; }
.image-name { font-weight: bold; margin-bottom: 5px; }
.image-actions { display: flex; justify-content: space-between; }

.comment-list { max-height: 200px; overflow-y: auto; margin-bottom: 10px; border: 1px solid #eee; padding: 5px; border-radius: 4px; }
.comment-item { margin-bottom: 5px; }
.no-comments { color: #999; text-align: center; }
</style>
