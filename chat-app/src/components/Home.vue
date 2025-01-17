<template>
  <div>
    <h2>Friend List</h2>
    <ul>
      <li v-for="(friend, index) in friends" :key="index">
        <button @click="navigateToChat(friend)">{{ friend }} - {{ isFriendOnline(friend.id)? 'Online' : 'Offline' }}</button>
      </li>
    </ul>
    <button @click="addFriend">Add Friend</button>
    <button @click="acceptFriend">Accept Friend</button>
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

    const addFriend = async () => {
      try {
        // 假设添加好友逻辑，例如添加用户 1 和用户 2 为好友
        await api.addFriendship(localStorage.getItem("loginId"), 3);
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
      navigateToChat
    };
  }
};
</script>