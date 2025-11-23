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
          <img :src="img.url" class="album-image" />
          <div class="image-name">{{ img.name }}</div>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const albumId = route.params.id
const albumName = ref('') // 可以通过 id 查找相册名称

const images = ref([
  { id: 1, name: '图片1', url: 'https://picsum.photos/300/200?random=11' },
  { id: 2, name: '图片2', url: 'https://picsum.photos/300/200?random=12' },
  { id: 3, name: '图片3', url: 'https://picsum.photos/300/200?random=13' }
])

// 面包屑返回
const goBack = () => {
  router.push('/user/album')
}

// 上传图片逻辑
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
    url: newImageForm.value.url
  })
  newImageForm.value = { name: '', url: '' }
  uploadDialogVisible.value = false
}

// 初始化相册名称（这里简单模拟）
onMounted(() => {
  const albumMap = {
    1: '旅游相册',
    2: '校园生活',
    3: '宠物'
  }
  albumName.value = albumMap[albumId] || '相册'
})
</script>

<style scoped>
.album-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
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
</style>
