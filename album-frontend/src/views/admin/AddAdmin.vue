<template>
  <el-card class="add-admin-card">
    <h3>添加管理员</h3>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="100px">
      
      <!-- 用户名 -->
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>
      
      <!-- 密码 -->
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>
      
      <!-- 确认密码 -->
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
      </el-form-item>
      
      <!-- 头像 -->
      <el-form-item label="头像" prop="avatarFile">
        <el-upload
        class="avatar-uploader"
        :show-file-list="false"
        :before-upload="beforeAvatarUpload"
        :on-change="handleAvatarChange"
        :auto-upload="false"
        >
        <div class="avatar-trigger" style="cursor: pointer; width: 80px; height: 80px; display: flex; align-items: center; justify-content: center; border: 1px dashed #d9d9d9; border-radius: 50%;">
            <img v-if="avatarPreview" :src="avatarPreview" class="avatar" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;" />
            <i v-else class="el-icon-plus avatar-placeholder" style="font-size: 28px; color: #999;"></i>
        </div>
        </el-upload>

      </el-form-item>
      
      <!-- 提交按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm">添加管理员</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const formRef = ref(null)

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  avatarFile: null
})

const avatarPreview = ref('')

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (value !== form.value.password) return Promise.reject(new Error('两次输入密码不一致'))
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ],
  avatarFile: [{ required: true, message: '请选择头像', trigger: 'change' }]
}

/** 头像选择 */
const handleAvatarChange = (file) => {
  form.value.avatarFile = file.raw
  avatarPreview.value = URL.createObjectURL(file.raw)
}

/** 上传前校验 */
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) ElMessage.error('只能选择图片文件')
  if (!isLt2M) ElMessage.error('图片大小不能超过 2MB')

  return isImage && isLt2M
}

/** 提交表单 */
const submitForm = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      const formData = new FormData()
      formData.append('username', form.value.username)
      formData.append('password', form.value.password)
      formData.append('avatar', form.value.avatarFile)

      const res = await axios.post('/api/admin/add', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })

      if (res.data.code === 200) {
        ElMessage.success('管理员添加成功')
        // 清空表单
        form.value.username = ''
        form.value.password = ''
        form.value.confirmPassword = ''
        form.value.avatarFile = null
        avatarPreview.value = ''
      } else {
        ElMessage.error(res.data.msg || '添加失败')
      }
    } catch (err) {
      console.error(err)
      ElMessage.error('请求失败，请稍后重试')
    }
  })
}
</script>

<style scoped>
.add-admin-card {
  max-width: 500px;
  margin: 20px auto;
}

.avatar-uploader {
  width: 80px;
  height: 80px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  font-size: 30px;
  color: #c0c4cc;
}
</style>
