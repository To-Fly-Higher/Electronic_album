<template>
  <div class="album-container">
    <el-row :gutter="20">
      <!-- 相册卡片 -->
      <el-col
        v-for="album in albums"
        :key="album.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card
          class="album-card"
          :body-style="{ padding: '0' }"
          shadow="hover"
        >
          <!-- 封面 -->
          <img
            :src="album.cover"
            class="album-cover"
            @click="openAlbum(album.id)"
          />
          <!-- 相册名 -->
          <div class="album-name">{{ album.name }}</div>

          <!-- 操作按钮，左右一行 -->
          <div class="album-operations">
            <el-button
              type="primary"
              size="small"
              @click.stop="editAlbum(album.id)"
            >
              修改
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click.stop="deleteAlbum(album.id)"
            >
              删除
            </el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 新建相册卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card
          class="album-card new-album"
          :body-style="{ padding: '0' }"
          shadow="hover"
          @click="createAlbum"
        >
          <div class="add-icon">+</div>
          <div class="album-name">新建相册</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新建/修改相册弹窗 -->
    <el-dialog :title="dialogTitle" v-model="createDialogVisible" width="400px">
      <el-form :model="newAlbumForm" label-width="80px">
        <el-form-item label="相册名">
          <el-input v-model="newAlbumForm.name" placeholder="请输入相册名"></el-input>
        </el-form-item>

        <el-form-item label="相册类别">
          <el-select v-model="newAlbumForm.category" placeholder="请选择类别">
            <el-option label="旅游" value="旅游"></el-option>
            <el-option label="校园" value="校园"></el-option>
            <el-option label="宠物" value="宠物"></el-option>
            <el-option label="生活" value="生活"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="封面图片">
          <el-upload
            class="upload-demo"
            action=""
            :auto-upload="false"
            :on-change="handleCoverChange"
            list-type="picture"
            :file-list="newAlbumForm.cover ? [{ name:'封面', url:newAlbumForm.cover }] : []"
          >
            <el-button size="small" type="primary">上传封面</el-button>
            <div slot="tip" class="el-upload__tip">只能上传图片文件</div>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAlbum">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const albums = ref([
  { id: 1, name: '旅游相册', category: '旅游', cover: 'https://picsum.photos/300/200?random=1' },
  { id: 2, name: '校园生活', category: '校园', cover: 'https://picsum.photos/300/200?random=2' },
  { id: 3, name: '宠物', category: '宠物', cover: 'https://picsum.photos/300/200?random=3' },
])

const openAlbum = (id) => {
  router.push(`/user/album/${id}`)
}

// 新建/修改弹窗逻辑
const createDialogVisible = ref(false)
const dialogTitle = ref('新建相册')
const editingAlbumId = ref(null)

const newAlbumForm = ref({
  name: '',
  category: '',
  cover: ''
})

const createAlbum = () => {
  dialogTitle.value = '新建相册'
  editingAlbumId.value = null
  newAlbumForm.value = { name: '', category: '', cover: '' }
  createDialogVisible.value = true
}

const handleCoverChange = (file) => {
  const reader = new FileReader()
  reader.readAsDataURL(file.raw)
  reader.onload = () => {
    newAlbumForm.value.cover = reader.result
  }
}

const submitAlbum = () => {
  if (!newAlbumForm.value.name || !newAlbumForm.value.category || !newAlbumForm.value.cover) {
    return alert('请填写完整信息')
  }

  if (editingAlbumId.value) {
    const index = albums.value.findIndex(a => a.id === editingAlbumId.value)
    albums.value[index] = { id: editingAlbumId.value, ...newAlbumForm.value }
  } else {
    const newId = albums.value.length + 1
    albums.value.push({ id: newId, ...newAlbumForm.value })
  }
  createDialogVisible.value = false
}

// 修改/删除方法
const editAlbum = (albumId) => {
  dialogTitle.value = '修改相册'
  editingAlbumId.value = albumId
  const album = albums.value.find(a => a.id === albumId)
  newAlbumForm.value = { ...album }
  createDialogVisible.value = true
}

const deleteAlbum = (albumId) => {
  const index = albums.value.findIndex(a => a.id === albumId)
  if (index !== -1) albums.value.splice(index, 1)
}
</script>

<style scoped>
.album-card {
  cursor: pointer;
  margin-bottom: 20px;
  text-align: center;
  position: relative;
}

.album-cover {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.album-name {
  padding: 10px 0;
  font-weight: bold;
  font-size: 16px;
}

/* 操作按钮左右显示 */
.album-operations {
  display: flex;
  justify-content: space-between;
  padding: 0 10px 10px 10px;
}

.new-album {
  border: 2px dashed #409EFF;
  color: #409EFF;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 150px;
}
.add-icon {
  font-size: 36px;
  line-height: 1;
  margin-top: 20px;
}
</style>
