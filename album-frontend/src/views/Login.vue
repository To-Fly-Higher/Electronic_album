<template>
  <div class="page-container">
    <el-card class="card">
      <h2 style="text-align:center">用户登录</h2>
      <login-form @login-success="handleLoginSuccess"/>
      <div style="margin-top: 10px; text-align:center;">
        <el-button type="text" @click="$router.push('/register')">没有账号？去注册</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import LoginForm from '../components/LoginForm.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const handleLoginSuccess = (role) => {
  if (role === 'admin') router.push('/admin')
  else router.push('/user')
}


</script>

<style scoped>
.page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  padding: 20px;
  width: 100vw;
}
.card {
  width: 100%;
  max-width: 400px;
  min-width: 300px;
}
</style>
