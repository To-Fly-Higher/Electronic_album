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
import { ref } from 'vue'

const categories = ref([
  { id: 1, name: '旅游' },
  { id: 2, name: '校园' },
  { id: 3, name: '宠物' },
])

const dialogVisible = ref(false)
const dialogTitle = ref('新建类别')
const editingCategoryId = ref(null)
const form = ref({ name: '' })

// 打开弹窗：新建或编辑
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

// 提交类别（新建或编辑）
const submitCategory = () => {
  if (!form.value.name.trim()) {
    return alert('请输入类别名称')
  }

  if (editingCategoryId.value) {
    // 编辑
    const index = categories.value.findIndex(c => c.id === editingCategoryId.value)
    if (index !== -1) categories.value[index].name = form.value.name.trim()
  } else {
    // 新建
    const newId = categories.value.length ? Math.max(...categories.value.map(c => c.id)) + 1 : 1
    categories.value.push({ id: newId, name: form.value.name.trim() })
  }
  dialogVisible.value = false
}

// 删除类别
const deleteCategory = (id) => {
  if (confirm('确定要删除吗？')) {
    const index = categories.value.findIndex(c => c.id === id)
    if (index !== -1) categories.value.splice(index, 1)
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
