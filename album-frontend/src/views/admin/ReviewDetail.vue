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
          <strong>{{ cmt.user.nickname }}:</strong> {{ cmt.content }}
          <el-button
            type="danger"
            size="mini"
            @click="deleteComment(cmt.id)"
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

// 接收路由参数
const userId = route.params.userId
const userName = route.params.userName
const albumId = route.params.albumId

const user = ref({ id: userId, name: userName })

const albumImages = ref([])

// 评论弹窗逻辑
const commentDialogVisible = ref(false)
const currentImage = ref(null)
const currentImageComments = ref([])
const commentList = ref(null)


const loadAlbum = async () => {
  try {
    const res = await axios.get(`/api/album/${albumId}/images`)
    if (res.data.code === 200 && Array.isArray(res.data.data)) {
      albumImages.value = res.data.data.map(img => ({
        id: img.id,
        name: img.name,
        url: fixUrl(img.url),
        likes: img.likes || [],          // 点赞用户列表
        comments: img.comments || []     // 评论列表
      }))
    }
  } catch (err) {
    console.error(err)
  }
}

// URL 补全函数（可以复用之前的）
const fixUrl = (url) => {
  if (!url) return ''
  if (/^https?:\/\//.test(url)) return url
  return `http://localhost:8080${url}`
}


const openComments = (image) => {
  currentImage.value = image
  currentImageComments.value = [...image.comments]
  commentDialogVisible.value = true
}



const scrollToBottom = () => {
  if (commentList.value) {
    commentList.value.scrollTop = commentList.value.scrollHeight
  }
}
// 删除评论
const deleteComment = (commentId) => {
  if (!confirm('确认删除该评论吗？')) return;
  console.log('Deleting comment with ID:', commentId);
  axios.delete(`/api/album/${commentId}`)
    .then(res => {
      if (res.data.code === 200) {
        // 删除本地数组
        const index = currentImageComments.value.findIndex(c => c.id === commentId);
        if (index !== -1) currentImageComments.value.splice(index, 1);

        const imgIndex = albumImages.value.findIndex(img => img.id === currentImage.value.id);
        if (imgIndex !== -1) {
          albumImages.value[imgIndex].comments = [...currentImageComments.value];
        }
      }
    }).catch(err => {
  console.error('Delete error:', err);
  ElMessage.error('删除失败');
});
};

// 删除图片
const deleteImage = (photoId) => {
  if (!confirm('确认删除该图片吗？')) return;

  axios.delete(`/api/album/${albumId}/image/${photoId}`)
    .then(res => {
      if (res.data.code === 200) {
        const index = albumImages.value.findIndex(img => img.id === photoId)
        if (index !== -1) albumImages.value.splice(index, 1)
      }
    }).catch(err => console.error(err));
};

const deleteAlbum = () => {
  if (confirm('确认删除该用户的公开相册吗？')) {
    // 调接口删除相册
    const id = albumId
    axios.delete(`/api/user/album/${id}`).then(res => {
      if (res.data.code === 200) router.back()
    }).catch(err => console.error(err))
  }
}

const goBack = () => {
  router.back()
}

// 页面加载时获取相册数据
onMounted(() => {
  console.log('Reviewing album for user:', userId, userName, albumId)
  loadAlbum()
})
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
