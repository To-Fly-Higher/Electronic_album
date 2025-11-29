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
            action=""
            :auto-upload="false"
            :on-change="handleImageChange"
            list-type="picture"
          >
            <el-button size="small" type="primary">选择图片</el-button>
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

const router = useRouter()
const route = useRoute()

const albumId = route.params.id
const albumName = ref('')

// ------------------ 模拟图片数据 ------------------
const images = ref([
  {
    id: 1,
    name: '图片1',
    url: 'https://picsum.photos/300/200?random=11',
    likes: [
      { id: 1, avatar: 'https://i.pravatar.cc/30?img=1' },
      { id: 2, avatar: 'https://i.pravatar.cc/30?img=2' }
    ],
    comments: [
      { id: 1, content: '好漂亮！', user: { id: 1, nickname: '小明', avatar: 'https://i.pravatar.cc/30?img=1' } },
      { id: 2, content: '喜欢这张', user: { id: 2, nickname: '小红', avatar: 'https://i.pravatar.cc/30?img=2' } }
    ]
  },
  {
    id: 2,
    name: '图片2',
    url: 'https://picsum.photos/300/200?random=12',
    likes: [
      { id: 3, avatar: 'https://i.pravatar.cc/30?img=3' }
    ],
    comments: [
      { id: 3, content: '赞一个', user: { id: 3, nickname: '小李', avatar: 'https://i.pravatar.cc/30?img=3' } }
    ]
  }
])

// ------------------ 面包屑返回 ------------------
const goBack = () => {
  router.push('/user/album')
}

// ------------------ 上传图片 ------------------
const uploadDialogVisible = ref(false)
const newImageForm = ref({ name: '', url: '' })

const uploadImage = () => {
  uploadDialogVisible.value = true
}

const handleImageChange = (file) => {
  const reader = new FileReader()
  reader.readAsDataURL(file.raw)
  reader.onload = () => {
    newImageForm.value.url = reader.result
  }
}

const submitNewImage = () => {
  if (!newImageForm.value.name || !newImageForm.value.url) return alert('请填写完整信息')
  const newId = images.value.length + 1
  images.value.push({
    id: newId,
    name: newImageForm.value.name,
    url: newImageForm.value.url,
    likes: [],
    comments: []
  })
  newImageForm.value = { name: '', url: '' }
  uploadDialogVisible.value = false
}

// ------------------ 图片预览 ------------------
const previewVisible = ref(false)
const previewUrl = ref('')

const showPreview = (url) => {
  previewUrl.value = url
  previewVisible.value = true
}

// ------------------ 删除图片 ------------------
const deleteImage = (id) => {
  images.value = images.value.filter(img => img.id !== id)
}

// ------------------ 动态弹窗 ------------------
const dynamicVisible = ref(false)
const selectedImage = ref(null)

const showDynamic = (img) => {
  selectedImage.value = img
  dynamicVisible.value = true
}
const downloadImage = (img) => {
  const link = document.createElement('a')
  link.href = img.url
  link.download = img.name || '图片'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}


// ------------------ 初始化相册名 ------------------
onMounted(() => {
  const albumMap = { 1: '旅游相册', 2: '校园生活', 3: '宠物' }
  albumName.value = albumMap[albumId] || '相册'
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
