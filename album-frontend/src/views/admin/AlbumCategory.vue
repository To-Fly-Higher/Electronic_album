<template>
  <div class="album-category-container">
    <!-- 顶部操作 -->
    <div class="header-actions" style="margin-bottom: 20px;">
      <el-button type="primary" @click="openDialog()">新建类别</el-button>
    </div>

    <!-- 类别表格 -->
    <el-table :data="categories" style="width: 100%">
      <el-table-column prop="name" label="类别名称" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="mini" type="primary" @click="openDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteCategory(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新建/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="类别名称">
          <el-input v-model="form.name" placeholder="请输入类别名称"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCategory">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'  // 如果你有 axios 封装
import { ElMessage } from 'element-plus'

const categories = ref([])

const dialogVisible = ref(false)
const dialogTitle = ref('新建类别')
const editingCategoryId = ref(null)
const form = ref({ name: '' })

// -------------- 获取类别列表 ----------------
const loadCategories = async () => {
  try {
    const res = await axios.get('/api/user/album/categories')
    if (res.data.code === 200) {
      categories.value = res.data.data
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('类别加载失败')
    console.error(err)
  }
}

// 页面初始化加载
onMounted(loadCategories)


// -------------- 打开弹窗（新建/编辑） --------------
const openDialog = (category = null) => {
  if (category) {
    dialogTitle.value = '编辑类别'
    editingCategoryId.value = category.id
    form.value.name = category.name
  } else {
    dialogTitle.value = '新建类别'
    editingCategoryId.value = null
    form.value.name = ''
  }
  dialogVisible.value = true
}


// -------------- 提交类别（新增 / 编辑） --------------
const submitCategory = async () => {
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入类别名称')
    return
  }

  try {
    if (editingCategoryId.value) {
      // -------- 编辑 --------
      const res = await axios.put(`/api/category/update/${editingCategoryId.value}`, {
        name: form.value.name.trim()
      })
      if (res.data.code === 200) {
        ElMessage.success('更新成功')
        loadCategories()
      } else {
        ElMessage.error(res.data.msg)
      }

    } else {
      // -------- 新建 --------
      const res = await axios.post('/api/category/add', {
        name: form.value.name.trim()
      })
      if (res.data.code === 200) {
        ElMessage.success('创建成功')
        loadCategories()
      } else {
        ElMessage.error(res.data.msg)
      }
    }

    dialogVisible.value = false

  } catch (err) {
    console.error(err)
    ElMessage.error('操作失败')
  }
}


// -------------- 删除类别 --------------
const deleteCategory = async (id) => {
  if (!confirm('确定删除吗？')) return
  try {
    const res = await axios.delete(`/api/category/delete/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadCategories()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.album-category-container {
  padding: 20px;
}
.header-actions {
  display: flex;
  justify-content: flex-start;
}
</style>
