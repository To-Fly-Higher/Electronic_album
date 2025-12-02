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
        <el-card class="album-card" :body-style="{ padding: '0' }" shadow="hover">
          <!-- <img :src="`http://localhost:8080${album.cover_url}`" class="album-cover" @click="openAlbum(album)" /> -->
          <img :src="album.cover_url" class="album-cover" @click="openAlbum(album)" />

          <div class="album-name">{{ album.name }}</div>
          <div class="album-operations">
            <el-button type="primary" size="small" @click.stop="editAlbum(album.id)">修改</el-button>
            <el-button type="danger" size="small" @click.stop="deleteAlbum(album.id)">删除</el-button>
            <el-button type="info" size="small" @click.stop="showRemark(album.remark)">备注</el-button>
          </div>
          <el-dialog title="相册备注" v-model="remarkDialogVisible" width="400px">
            <p>{{ currentRemark }}</p>
            <span slot="footer">
              <el-button @click="remarkDialogVisible = false">关闭</el-button>
            </span>
          </el-dialog>
        </el-card>
      </el-col>

      <!-- 新建相册卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="album-card new-album" :body-style="{ padding: '0' }" shadow="hover" @click="createAlbum">
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
          <el-select v-model="newAlbumForm.category_id" placeholder="请选择类别">
            <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="封面图片">
          <!-- <el-upload
            class="upload-demo"
            action="/api/user/upload"
            :limit="1"
            list-type="picture"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :file-list="newAlbumForm.cover_url ? [{ name: '封面', url: newAlbumForm.cover_url }] : []"
          >
            <el-button size="small" type="primary">上传封面</el-button>
            <div slot="tip" class="el-upload__tip">只能上传图片文件</div>
          </el-upload> -->
          <el-upload
            class="upload-demo"
            :auto-upload="false"
            :limit="1"
            list-type="picture"
            :file-list="newAlbumForm.cover_preview ? [{ name: '封面', url: newAlbumForm.cover_preview }] : []"

            :on-change="handleFileChange"
          >
            <el-button size="small" type="primary">选择封面</el-button>
            <div slot="tip" class="el-upload__tip">只能上传图片文件</div>
          </el-upload>

        </el-form-item>

        <el-form-item label="相册备注">
          <el-input
            type="textarea"
            v-model="newAlbumForm.remark"
            placeholder="请输入相册备注"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否公开">
          <el-radio-group v-model="newAlbumForm.is_public">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="0">私密</el-radio>
          </el-radio-group>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const albums = ref([])
const categories = ref([])

const remarkDialogVisible = ref(false)
const currentRemark = ref('')

const createDialogVisible = ref(false)
const dialogTitle = ref('新建相册')
const editingAlbumId = ref(null)
const newAlbumForm = ref({ name: '', category_id: null, cover_url: '', remark: '', is_public: 0 })

// 获取当前用户信息
const user = JSON.parse(localStorage.getItem('user'))
const userId = user?.id
// 打开相册
const openAlbum = (album) => {
  router.push({
    path: `/user/album/${album.id}`,
    query: { name: album.name }
  })
}
// 加载相册列表
// const loadAlbums = async () => {
//   if (!userId) return
//   try {
//     const res = await axios.get('/api/user/album/list', { params: { userId } })
//     console.log(res.data)
//     if (res.data.code === 200) albums.value = res.data.data
//     else ElMessage.error(res.data.msg)
//   } catch {
//     ElMessage.error('相册加载失败')
//   }
// }
// 加载相册列表
const loadAlbums = async () => {
  if (!userId) return
  try {
    const res = await axios.get('/api/user/album/list', { params: { userId } })
    if (res.data.code === 200) {
      albums.value = res.data.data.map(album => ({
        ...album,
        cover_url: fixUrl(album.cover_url)
      }))
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch {
    ElMessage.error('相册加载失败')
  }
}

// URL 补全函数
const fixUrl = (url) => {
  if (!url) return ''
  // 如果已经是 http 或 https 开头，直接返回
  if (/^https?:\/\//.test(url)) return url
  // 不是 http，补全
  return `http://localhost:8080${url}`
}


// 加载相册类别
const loadCategories = async () => {
  try {
    const res = await axios.get('/api/user/album/categories')
    if (res.data.code === 200) {
      categories.value = res.data.data.map(item => ({
        value: item.id,
        label: item.name
      }))
    } else {
      ElMessage.error('相册类别获取失败')
    }
  } catch {
    ElMessage.error('相册类别获取失败')
  }
}

// 新建/修改弹窗
const createAlbum = () => {
  dialogTitle.value = '新建相册'
  editingAlbumId.value = null
  newAlbumForm.value = { name: '', category_id: null, cover_url: '', remark: '', is_public: 0 }
  createDialogVisible.value = true
}

// 上传封面处理
const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    newAlbumForm.value.cover_url = res.data.url
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}
// 保存选中的文件对象
const handleFileChange = (file) => {
  newAlbumForm.value.cover_file = file.raw
  // 用于显示预览
  newAlbumForm.value.cover_preview = URL.createObjectURL(file.raw)
}

const handleUploadError = () => {
  ElMessage.error('上传失败，请重试')
}


const submitAlbum = async () => {
  if (!newAlbumForm.value.name || !newAlbumForm.value.category_id || !newAlbumForm.value.cover_file) {
    return ElMessage.warning('请填写完整信息')
  }

  const formData = new FormData()
  formData.append('name', newAlbumForm.value.name)
  formData.append('categoryId', newAlbumForm.value.category_id)
  formData.append('remark', newAlbumForm.value.remark || '')
  formData.append('isPublic', newAlbumForm.value.is_public)
  formData.append('userId', userId)
  formData.append('coverFile', newAlbumForm.value.cover_file)

  try {
    let res
    if (editingAlbumId.value) {
      res = await axios.put(`/api/user/album/${editingAlbumId.value}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    } else {
      res = await axios.post('/api/user/album', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    }

    if (res.data.code === 200) {
      loadAlbums()
      createDialogVisible.value = false
      ElMessage.success(editingAlbumId.value ? '修改成功' : '创建成功')
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch {
    ElMessage.error('操作失败')
  }
}

// 修改相册
// const editAlbum = (albumId) => {
//   const album = albums.value.find(a => a.id === albumId)
//   if (!album) return
//   dialogTitle.value = '修改相册'
//   editingAlbumId.value = albumId
//   newAlbumForm.value = { ...album }
//   createDialogVisible.value = true
// }
const editAlbum = (albumId) => {
  const album = albums.value.find(a => a.id === albumId)
  if (!album) return
  dialogTitle.value = '修改相册'
  editingAlbumId.value = albumId

  // 回显封面图片
  newAlbumForm.value = { 
    ...album,
    cover_file: null, // 没有新上传文件
    cover_preview: `http://localhost:8080${album.cover_url}` // 用现有图片 URL 显示
  }
  createDialogVisible.value = true
}


// 删除相册
const deleteAlbum = (albumId) => {
  const album = albums.value.find(a => a.id === albumId)
  if (!album) return
  axios.delete(`/api/user/album/${albumId}`)
    .then(res => {
      if (res.data.code === 200) {
        albums.value = albums.value.filter(a => a.id !== albumId)
        ElMessage.success('删除成功')
      } else {
        ElMessage.error(res.data.msg)
      }
    })
    .catch(() => ElMessage.error('删除失败'))
}

// 显示备注
const showRemark = (remark) => {
  currentRemark.value = remark || '暂无备注'
  remarkDialogVisible.value = true
}

onMounted(() => {
  loadAlbums()
  loadCategories()
})
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
