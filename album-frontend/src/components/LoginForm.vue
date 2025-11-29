<template>
  <el-form :model="form" ref="loginForm" :rules="rules" label-width="80px">
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
    </el-form-item>

    <el-form-item label="密码" prop="password">
      <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm">登录</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const emit = defineEmits(['login-success'])

const submitForm = async () => {
  try {
    // 调用后端登录接口
    const response = await axios.post('/api/user/login', {
      username: form.value.username,
      password: form.value.password
    })

    if (response.data.code === 200) {
      const userData = response.data.data
      const role = userData.role === 1 ? 'admin' : 'user'

      // 保存 token 和用户信息到 localStorage
      localStorage.setItem('token', userData.token)
      localStorage.setItem('user', JSON.stringify(userData))

      // 通知父组件登录成功并传递角色
      emit('login-success', role)
    } else {
      ElMessage.error(response.data.msg || '账号或密码错误')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('接口请求失败')
  }
}

</script>
