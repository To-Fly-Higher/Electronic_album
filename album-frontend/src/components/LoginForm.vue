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

const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const emit = defineEmits(['login-success'])

// const submitForm = () => {
//   // 这里先写前端模拟登录
//   if (form.value.username === 'admin') {
//     emit('login-success', 'admin')
//   } else {
//     emit('login-success', 'user')
//   }
// }
const submitForm = () => {
  if (form.value.username === 'admin' && form.value.password === '123') {
    emit('login-success', 'admin')   // 管理员
  } else if (form.value.username === 'user' && form.value.password === '123') {
    emit('login-success', 'user')    // 用户
  } else {
    ElMessage.error('账号或密码错误')
  }
}
</script>
