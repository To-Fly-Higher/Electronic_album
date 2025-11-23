<template>
  <div>
    <h2>通知列表</h2>
    <el-card v-for="(note, index) in notifications" :key="index" class="notification-card">
      <p><strong>{{ note.title }}</strong></p>
      <p>{{ note.content }}</p>
      <p class="time">{{ note.time }}</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const notifications = ref([
  { title: '系统通知', content: '你的相册被点赞了！', time: '2025-11-23 12:00' },
  { title: '好友动态', content: 'Alice 发布了新相册', time: '2025-11-22 18:30' },
  { title: '系统通知', content: '你的评论有人回复了', time: '2025-11-21 09:45' }
])
</script>

<style scoped>
.notification-card {
  margin-bottom: 10px;
}

.time {
  font-size: 12px;
  color: #999;
}
</style>
