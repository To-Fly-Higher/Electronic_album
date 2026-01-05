<template>
  <div class="friends-container">
    <el-row :gutter="20">
      <!-- 好友和请求列表 -->
      <el-col
        v-for="friend in visibleFriends"
        :key="friend.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card
          class="friend-card"
          shadow="hover"
          :class="{ 'friend-pending': friend.status === 0 && friend.type === 'sent' }"
        >
          <div class="friend-info">
            <img :src="friend.avatar" alt="头像" class="friend-avatar" />
            <div class="friend-name">{{ friend.name }}</div>
          </div>

          <!-- 已成为好友 -->
          <el-button
            v-if="friend.type === 'friend'"
            type="primary"
            size="small"
            class="friend-action"
            @click="viewFriendAlbum(friend.id)"
          >
            查看动态
          </el-button>

          <!-- 当前用户发出的请求 -->
          <el-button
            v-else-if="friend.type === 'sent'"
            type="primary"
            size="small"
            class="friend-action"
            disabled
          >
            等待确认
          </el-button>

          <!-- 当前用户收到的请求 -->
          <div v-else-if="friend.type === 'received'" class="request-actions">
            <el-button
              type="success"
              size="small"
              @click="handleRequest(friend.id, 1)"
            >同意</el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleRequest(friend.id, 2)"
            >拒绝</el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 添加好友卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="friend-card add-friend-card" shadow="hover" @click="showAddFriendDialog">
          <div class="friend-info">
            <div class="add-icon">+</div>
            <div class="friend-name">添加好友</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加好友弹窗 -->
    <el-dialog
      title="添加好友"
      v-model="addFriendDialogVisible"
      width="400px"
      :destroy-on-close="true"
    >
      <el-form>
        <el-form-item label="好友昵称">
          <el-input v-model="searchName" placeholder="请输入好友昵称"></el-input>
        </el-form-item>
      </el-form>
      <div v-if="searchResult" class="search-result">
        <div>{{ searchResult.nickname }}</div>
        <el-button type="primary" size="small" @click="sendFriendRequest(searchResult.id)">
          发送好友请求
        </el-button>
      </div>
      <template #footer>
        <el-button @click="addFriendDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="searchFriend">搜索</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const friends = ref([])
const userId = JSON.parse(localStorage.getItem('user') || '{}').id;
// 前端直接处理好的列表，包含好友和请求
const visibleFriends = computed(() => friends.value.filter(f => f))

// URL 补全函数
const fixUrl = (url) => {
  if (!url) return ''
  // 如果已经是 http 或 https 开头，直接返回
  if (/^https?:\/\//.test(url)) return url
  // 不是 http，补全
  return `http://localhost:8080${url}`
}

const loadFriends = async () => {
  if (!userId) return ElMessage.warning('请先登录')

  try {
    const res = await axios.get('/api/friend/list', {
      params: { user_id: userId } // 带上用户ID
    })

    if (res.data.code === 200 && res.data.data) {
      friends.value = res.data.data;  // 直接赋值
      friends.value.forEach(friend => {
        friend.avatar = fixUrl(friend.avatar);
      });
      
      console.log('好友列表加载成功');
      console.log(res.data.data);
      console.log(friends.value);     // 检查是否有数据
    } else {
      friends.value = [];
      ElMessage.info(res.data.msg || '暂无好友');
    }

  } catch (err) {
    console.error(err)
    ElMessage.error('好友列表加载失败')
  }
}

const viewFriendAlbum = (friendId) => router.push(`/user/friend-album/${friendId}`)

// ----------------- 添加好友 -----------------
const addFriendDialogVisible = ref(false)
const searchName = ref('')
const searchResult = ref(null)
const showAddFriendDialog = () => {
  addFriendDialogVisible.value = true
  searchName.value = ''
  searchResult.value = null
}

// 搜索好友
const searchFriend = async () => {
  if (!searchName.value) return ElMessage.warning('请输入昵称')

  try {
    const res = await axios.get(`/api/friend/search`, { 
      params: { name: searchName.value }
    })
    if (res.data.code === 200 && res.data.data) {
      searchResult.value = res.data.data
    } else {
      searchResult.value = null
      ElMessage.info('未找到该用户')
    }
  } catch {
    ElMessage.error('搜索失败')
  }
}


// 发送好友请求
const sendFriendRequest = async (friendId) => {
  if (!userId) return ElMessage.warning('请先登录')

  try {
    const res = await axios.post(
      `/api/friend/request`,
      { user_id: userId, friend_id: friendId }
    )
    if (res.data.code === 200) {
      ElMessage.success('好友请求已发送')
      addFriendDialogVisible.value = false
    } else {
      ElMessage.error(res.data.msg || '发送失败')
    }
  } catch {
    ElMessage.error('发送失败')
  }
}


// 处理收到的好友请求（同意/拒绝）
const handleRequest = async (requestId, action) => {
  if (!userId) return ElMessage.warning('请先登录')

  try {
    const res = await axios.post(
      '/api/friend/handle-request',
      { user_id: userId, request_id: requestId, action }
    )
    if (res.data.code === 200) {
      ElMessage.success(action === 1 ? '已同意' : '已拒绝')
      // 刷新列表
      loadFriends()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch {
    ElMessage.error('操作失败')
  }
}


onMounted(loadFriends)
</script>

<style scoped>
.friends-container {
  padding: 20px;
}

.el-row {
  margin-bottom: 20px; /* 每行卡片之间的间距 */
}

.friend-card {
  display: flex;
  flex-direction: column;
  align-items: center; /* 内容水平居中 */
  justify-content: center; /* 内容垂直居中 */
  text-align: center;
  padding: 20px;
  min-height: 220px; /* 卡片高度，可根据需要调整 */
  transition: opacity 0.3s;
  margin-bottom: 20px; /* 每个卡片之间的上下间距 */
}

.friend-pending {
  opacity: 0.5;
  pointer-events: none;
}

.friend-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 15px;
}

.friend-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 10px; /* 图片与名字之间间距 */
}

.friend-name {
  font-weight: bold;
  font-size: 16px;
}

.friend-action {
  width: 100%;
}

.pending-text {
  font-size: 12px;
  color: #999;
  margin-left: 5px;
}

.add-friend-card {
  cursor: pointer;
  color: #409EFF;
}

.add-icon {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 5px;
}

.search-result {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

</style>
