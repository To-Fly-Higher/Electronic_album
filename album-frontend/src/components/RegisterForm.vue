<template>
  <el-form :model="form" ref="registerForm" :rules="rules" label-width="100px">
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
    </el-form-item>

    <el-form-item label="密码" prop="password">
      <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
    </el-form-item>

    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
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

const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

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
  ]
}

const emit = defineEmits(['register-success'])

const submitForm = async () => {
  try {
    // 调用后端注册接口
    const res = await axios.post('/api/user/register', {
      username: form.value.username,
      password: form.value.password
    })

    if (res.data.code === 200) {
      ElMessage.success('注册成功')
      emit('register-success')
      // console.log('注册成功，跳转到登录页面')
    } else {
      ElMessage.error(res.data.msg || '注册失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败，请稍后重试')
  }
}
</script>
