<template>
  <el-form :model="form" ref="registerForm" :rules="rules" label-width="100px">

    <!-- 头像选择 -->
    <el-form-item label="头像" prop="avatar">
      <el-upload
        class="avatar-uploader"
        :show-file-list="false"
        :auto-upload="false"
        :on-change="handleAvatarChange"
        :before-upload="beforeAvatarUpload"
      >
        <img v-if="avatarPreview" :src="avatarPreview" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon">
          <Plus />
        </el-icon>
      </el-upload>
    </el-form-item>

    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" placeholder="请输入用户名" />
    </el-form-item>

    <el-form-item label="密码" prop="password">
      <el-input v-model="form.password" type="password" placeholder="请输入密码" />
    </el-form-item>

    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input
        v-model="form.confirmPassword"
        type="password"
        placeholder="请再次输入密码"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm">注册</el-button>
    </el-form-item>

  </el-form>
</template>


<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  avatarFile: null   // 保存原始文件
})

const avatarPreview = ref('')

const rules = {
  avatar: [{ required: true, message: '请选择头像', trigger: 'change' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (value !== form.value.password) {
          return Promise.reject(new Error('两次输入密码不一致'))
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

/** 选择头像 */
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

/** 提交注册（FormData） */
const submitForm = async () => {
  try {
    const formData = new FormData()
    formData.append('username', form.value.username)
    formData.append('password', form.value.password)
    formData.append('avatar', form.value.avatarFile)

    const res = await axios.post('/api/user/register', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (res.data.code === 200) {
      ElMessage.success('注册成功')
      router.push('/login')   // 跳转到登录页面
    } else {
      ElMessage.error(res.data.msg || '注册失败')
    }
  } catch (err) {
    ElMessage.error('请求失败，请稍后重试')
  }
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
}

.avatar-uploader-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 1px dashed #d9d9d9;
  text-align: center;
  line-height: 80px;
  font-size: 28px;
  color: #8c939d;
}
</style>
