<template>
  <div class="album-detail-container">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click.native="goBack" style="cursor:pointer">相册</el-breadcrumb-item>
      <el-breadcrumb-item>{{ albumName }}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 图片瀑布流 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col
        v-for="img in images"
        :key="img.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card :body-style="{ padding: '0' }" shadow="hover">
          <!-- 点击图片弹大图 -->
          <img :src="img.url" class="album-image" @click="showPreview(img.url)" />

          <div class="image-name">{{ img.name }}</div>

          <!-- 按钮行 -->
          <div class="image-buttons">
            <el-button size="mini" type="danger" @click="deleteImage(img.id)">删除</el-button>
            <el-button size="mini" type="success" @click="downloadImage(img)">下载</el-button>
            <el-button size="mini" type="primary" @click="showDynamic(img)">动态</el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 上传图片卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card
          class="upload-card"
          :body-style="{ padding: '0' }"
          shadow="hover"
          @click="uploadImage"
        >
          <div class="add-icon">+</div>
          <div class="image-name">上传图片</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 上传图片对话框 -->
    <el-dialog title="上传图片" v-model="uploadDialogVisible">
      <el-form :model="newImageForm">
        <el-form-item label="图片名">
          <el-input v-model="newImageForm.name" placeholder="请输入图片名"></el-input>
        </el-form-item>
          <el-form-item label="图片文件">
            <el-upload
              class="upload-demo"
              action="/api/user/upload"       
              :limit="1"
              list-type="picture"
              :on-success="handleUploadSuccess" 
              :on-error="handleUploadError"
              :file-list="newImageForm.url ? [{ name: '图片', url: newImageForm.url }] : []"
            >
              <el-button size="small" type="primary">上传图片</el-button>
              <div slot="tip" class="el-upload__tip">只能上传图片文件</div>
            </el-upload>
          </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="uploadDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitNewImage">确认</el-button>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" width="50%" :show-close="true" :close-on-click-modal="true">
      <img :src="previewUrl" style="width:100%" />
    </el-dialog>

    <!-- 动态弹窗 -->
    <el-dialog title="动态" v-model="dynamicVisible" width="400px">
      <div v-if="selectedImage">
        <h4>点赞</h4>
        <div class="likes">
          <img
            v-for="user in selectedImage.likes"
            :key="user.id"
            :src="user.avatar"
            class="avatar"
          />
        </div>
        <h4>评论</h4>
        <div class="comments">
          <div v-for="comment in selectedImage.comments" :key="comment.id" class="comment-item">
            <img :src="comment.user.avatar" class="avatar-small" />
            <span><b>{{ comment.user.nickname }}:</b> {{ comment.content }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const albumId = route.params.id
const albumName = ref(route.query.name || '相册')

// ------------------ 图片数据 ------------------
const images = ref([])

// ------------------ 面包屑返回 ------------------
const goBack = () => {
  router.push('/user/album')
}

// ------------------ 上传图片 ------------------
const uploadDialogVisible = ref(false)
const newImageForm = ref({ name: '', url: '' })

const uploadImage = () => { uploadDialogVisible.value = true }

const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    // 直接把返回的 URL 存到 newImageForm
    newImageForm.value.url = res.data.url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}

const handleUploadError = () => {
  ElMessage.error('上传失败，请重试')
}

const submitNewImage = async () => {
  if (!newImageForm.value.name || !newImageForm.value.url) {
    return ElMessage.warning('请填写完整信息')
  }

  // 提交的时候只传 URL 和图片名
  try {
    await axios.post(`/api/album/${albumId}/image`, {
      name: newImageForm.value.name,
      url: newImageForm.value.url
    })
    ElMessage.success('提交成功')
    newImageForm.value = { name: '', url: '' }
    uploadDialogVisible.value = false
    loadImages() // 刷新图片列表
  } catch {
    ElMessage.error('提交失败')
  }
}


// ------------------ 加载图片 ------------------
const loadImages = async () => {
  try {
    const res = await axios.get(`/api/album/${albumId}/images`)
    images.value = res.data.data || []
  } catch {
    ElMessage.error('图片加载失败')
  }
}

// ------------------ 图片预览 ------------------
const previewVisible = ref(false)
const previewUrl = ref('')

const showPreview = (url) => {
  previewUrl.value = url
  previewVisible.value = true
}

// ------------------ 删除图片 ------------------
const deleteImage = async (id) => {
  try {
    await axios.delete(`/api/album/${albumId}/image/${id}`)
    images.value = images.value.filter(img => img.id !== id)
    ElMessage.success('删除成功')
  } catch {
    ElMessage.error('删除失败')
  }
}

// ------------------ 动态弹窗 ------------------
const dynamicVisible = ref(false)
const selectedImage = ref(null)

const showDynamic = (img) => {
  selectedImage.value = img
  dynamicVisible.value = true
}

// ------------------ 下载图片 ------------------
const downloadImage = (img) => {
  const link = document.createElement('a')
  link.href = img.url
  link.download = img.name || '图片'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

onMounted(() => {
  loadImages()
})
</script>


<style scoped>
.album-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  cursor: pointer;
}
.image-name {
  padding: 10px 0;
  font-weight: bold;
  text-align: center;
}
.upload-card {
  border: 2px dashed #409EFF;
  color: #409EFF;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 150px;
  cursor: pointer;
}
.add-icon {
  font-size: 36px;
  line-height: 1;
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.image-buttons {
  display: flex;
  justify-content: space-around;
  padding: 5px 0 10px 0;
}
.likes {
  display: flex;
  margin-bottom: 10px;
}
.likes .avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 5px;
}
.comments .comment-item {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}
.comments .avatar-small {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  margin-right: 5px;
}
</style>
