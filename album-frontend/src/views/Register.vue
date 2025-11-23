<template>
  <div class="page-container">
    <el-card class="card">
      <h2 style="text-align:center">用户注册</h2>
      <register-form @register-success="handleRegisterSuccess" />
      <div style="margin-top: 10px; text-align:center;">
        <el-button type="text" @click="$router.push('/login')">已有账号？去登录</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import RegisterForm from '../components/RegisterForm.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const handleRegisterSuccess = () => {
  router.push('/login')
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
  width: 90%;
  max-width: 400px;
  min-width: 300px;
}
</style>
