<template>
  <div class="friends-container">
    <el-row :gutter="20">
      <el-col
        v-for="friend in friends"
        :key="friend.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card class="friend-card" shadow="hover">
          <div class="friend-info">
            <img :src="friend.avatar" alt="头像" class="friend-avatar" />
            <div class="friend-name">{{ friend.name }}</div>
          </div>
          <el-button
            type="primary"
            size="small"
            class="friend-action"
            @click="viewFriendAlbum(friend.id)"
          >
            查看动态
          </el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const friends = ref([
  { id: 1, name: 'Alice', avatar: 'https://i.pravatar.cc/100?img=1' },
  { id: 2, name: 'Bob', avatar: 'https://i.pravatar.cc/100?img=2' },
  { id: 3, name: 'Charlie', avatar: 'https://i.pravatar.cc/100?img=3' },
])

const viewFriendAlbum = (friendId) => {
  // 跳转到好友公开相册页面，可以传 friendId
  router.push(`/user/friend-album/${friendId}`)
}
</script>

<style scoped>
.friends-container {
  padding: 20px;
}

.friend-card {
  text-align: center;
  padding: 20px;
}

.friend-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
}

.friend-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 5px;
}

.friend-name {
  font-weight: bold;
  font-size: 16px;
}

.friend-action {
  width: 100%;
}
</style>
