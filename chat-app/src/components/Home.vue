<template>
  <div>
    <h2>Friend List</h2>
    <ul>
      <li v-for="(friend, index) in friends" :key="index">
        <button @click="navigateToChat(friend)">{{ friend }} - {{ isFriendOnline(friend.id)? 'Online' : 'Offline' }}</button>
      </li>
    </ul>
    <input type="text" v-model="searchQuery" placeholder="Search friends by name or id" />
    <button @click="searchFriends">Search</button>
    <button @click="addFriend">Add Friend</button>
    <button @click="acceptFriend">Accept Friend</button>
    <!-- 新增下拉框展示搜索到的好友 -->
    <select v-model="selectedFriend" @change="handleSelectChange">
      <option v-for="friend in searchResults" :key="friend.id" :value="friend">{{ friend.username === null? "" : friend.username }}</option>
    </select>
    <!-- 新增添加好友模态框 -->
    <div class="modal" v-if="showAddFriendModal && selectedFriend">
      <div class="modal-content">
        <span class="close" @click="hideAddFriendModal">&times;</span>
        <p>Do you want to add {{ selectedFriend.username }} as a friend?</p>
        <button @click="addFriend(selectedFriend.id)">Yes</button>
        <button @click="hideAddFriendModal">No</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import api from '../service/api';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const friends = ref([]);
    const router = useRouter();
    let socket;
    const searchQuery = ref('');
    const selectedFriend = ref(null);
    let showAddFriendModal = ref(false);
    const searchResults = ref([]);

    const connectWebSocket = () => {
      socket = new WebSocket('ws://localhost:8089/chat');
      socket.onopen = () => {
        console.log('WebSocket connected');
      };
      socket.onmessage = (event) => {
        // 处理消息更新好友状态
      };
      socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
      socket.onclose = () => {
        console.log('WebSocket closed');
      };
    };

    const isFriendOnline = (friendId) => {
      // 假设调用后端 API 或使用 WebSocket 检查好友是否在线
      return false;
    };

    const addFriend = async (friendId) => {
      try {
        // 假设添加好友逻辑，例如添加用户 1 和用户 2 为好友
        await api.addFriendship(localStorage.getItem("loginId"), selectedFriend.value.id);
        console.log('Friend added');
      } catch (error) {
        console.error('Error adding friend:', error);
      }
    };

    const acceptFriend = async () => {
      try {
        // 假设接受好友逻辑，例如用户 1 接受用户 2 的好友请求
        await api.acceptFriendship(localStorage.getItem("loginId"), 3);
        console.log('Friend accepted');
      } catch (error) {
        console.error('Error accepting friend:', error);
      }
    };

    const navigateToChat = (friendId) => {
      // 假设用户 ID 为 1
      router.push({ name: 'Chat', params: { senderId: localStorage.getItem("loginId"), receiverId: friendId } });
    };

    const searchFriends = async () => {
      try {
        // 假设搜索好友逻辑，根据搜索Query调用后端API搜索好友
        const response = await api.searchFriends(searchQuery.value);
        searchResults.value = response.data;
      } catch (error) {
        console.error('Error searching friends:', error);
      }
    };

    const handleSelectChange = () => {
      if (selectedFriend.value) {
        showAddFriendModal.value = true;
      }
    };

    const hideAddFriendModal = () => {
      showAddFriendModal.value = false;
    };

    onMounted(async () => {
      try {
        // 假设用户 ID 为 1
        friends.value = (await api.getAcceptedFriends(localStorage.getItem("loginId"))).data;
        connectWebSocket();
      } catch (error) {
        console.error('Error fetching friends:', error);
      }
    });

    return {
      friends,
      isFriendOnline,
      addFriend,
      acceptFriend,
      navigateToChat,
      searchQuery,
      searchFriends,
      selectedFriend,
      showAddFriendModal,
      hideAddFriendModal,
      searchResults
    };
  }
};
</script>

<!-- 样式 -->
<style>
.hello {
  text-align: center;
}
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.modal {
  display: none;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}
.modal-content {
           background-color: #fefefe;
           margin: 15% auto;
           padding: 20px;
           border: 1px solid #888;
           width: 30%;
         }
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}
.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>