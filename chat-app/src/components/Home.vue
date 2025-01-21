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

    <!-- 好友请求列表 -->
    <h3>Friend Requests</h3>
    <ul>
      <li v-for="(request, index) in friendRequests" :key="index">
        <span>{{ request.username }} sent you a friend request</span>
        <span v-if="request.status==='pending'">
          <button  @click="acceptFriendRequest(request.id)">Accept</button>
        <button @click="rejectFriendRequest(request.id)">Reject</button>
        </span>

      </li>
    </ul>

    <!-- 新增下拉框展示搜索到的好友 -->
    <select v-model="selectedFriend" @change="handleSelectChange">
      <option v-for="friend in searchResults" :key="friend.id" :value="friend">{{ friend.username || '' }}</option>
    </select>
    <!-- 新增添加好友模态框 -->
    <div class="modal" v-if="showAddFriendModal && selectedFriend">
      <div class="modal-content">
        <span class="close" @click="hideAddFriendModal">&times;</span>
        <p>Do you want to add {{ selectedFriend.username }} as a friend?</p>
        <button @click="confirmAddFriend">Yes</button>
        <button @click="hideAddFriendModal">No</button>
      </div>
    </div>
    <div v-if="friendRequestNotification" class="friend-request-notification">
      <p>{{ friendRequestNotification }}</p>
      <button @click="acceptFriendRequestNotification">Accept</button>
      <button @click="rejectFriendRequestNotification">Reject</button>
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
    const searchQuery = ref('');
    const selectedFriend = ref(null);
    const showAddFriendModal = ref(false);
    const searchResults = ref([]);
    const friendRequestNotification = ref('');
    const friendRequests = ref([]); // 新增：存储好友请求列表
    let chatSocket;
    let notificationSocket;

    const connectWebSocket = (url, onMessageHandler) => {
      const socket = new WebSocket(url);
      socket.onopen = () => console.log(`${url} WebSocket connected`);
      socket.onmessage = onMessageHandler;
      socket.onerror = (error) => console.error(`${url} WebSocket error:`, error);
      socket.onclose = () => console.log(`${url} WebSocket closed`);
      return socket;
    };

    const connectChatWebSocket = () => {
      chatSocket = connectWebSocket('ws://localhost:8089/chat', (event) => {
        console.log("Received chat message:", event.data);
      });
    };

    const connectNotificationWebSocket = () => {
      const userId = localStorage.getItem("loginId");
      notificationSocket = connectWebSocket(`ws://localhost:8089/notification?userId=${userId}`, (event) => {
        console.log("Received notification message:", event.data);
        const result = confirm("您有一个好友请求是否接受"+JSON.parse(event.data).username);
        if (result) {
          acceptFriendRequest(JSON.parse(event.data).id)
        } else {
          console.log("忽略");
          fetchFriendRequests();
        }


        if (event.data.startsWith("friend_request:")) {
          friendRequestNotification.value = event.data.split(":")[1];
        }
      });
    };

    const isFriendOnline = (friendId) => {
      // 假设调用后端 API 或使用 WebSocket 检查好友是否在线
      return false;
    };

    const addFriend = async () => {
      try {
        await api.addFriendship(localStorage.getItem("loginId"), selectedFriend.value.id);
        console.log('Friend request sent');
        sendFriendRequestNotification(selectedFriend.value.id);
      } catch (error) {
        console.error('Error adding friend:', error);
      }
    };

    const acceptFriend = async (id) => {
      try {
        console.log("id"+id)
        await api.acceptFriendship(localStorage.getItem("loginId"), id);
        console.log('Friend accepted');
      } catch (error) {
        console.error('Error accepting friend:', error);
      }
    };

    // 新增：接受好友请求列表中的请求
    const acceptFriendRequest = async (requestId) => {
      try {
        console.log("requestId"+requestId)
        await api.acceptFriendship(localStorage.getItem("loginId"),requestId);
        // 刷新好友请求列表
        fetchFriendRequests();
      } catch (error) {
        fetchFriendRequests();
        console.error('Error accepting friend request:', error);
      }
    };

    // 新增：拒绝好友请求列表中的请求
    const rejectFriendRequest = async (requestId) => {
      try {
        await api.rejectFriendship(localStorage.getItem("loginId"),requestId);
        // 刷新好友请求列表
        fetchFriendRequests();
      } catch (error) {
        console.error('Error rejecting friend request:', error);
      }
    };

    const acceptFriendRequestNotification = async () => {
      try {
        const senderId = friendRequestNotification.value.split(' ')[4];
        await acceptFriend(parseInt(senderId));
        friendRequestNotification.value = '';
      } catch (error) {
        console.error('Error accepting friend request:', error);
      }
    };

    const rejectFriendRequestNotification = () => {
      friendRequestNotification.value = '';
    };

    const navigateToChat = (friendId) => {
      router.push({ name: 'Chat', params: { senderId: localStorage.getItem("loginId"), receiverId: friendId } });
    };

    const searchFriends = async () => {
      try {
        const response = await api.searchFriends(searchQuery.value);
        searchResults.value = response.data;
      } catch (error) {
        console.error('Error searching friends:', error);
      }
    };

    const handleSelectChange = () => {
      showAddFriendModal.value = true;
    };

    const hideAddFriendModal = () => {
      showAddFriendModal.value = false;
    };

    const confirmAddFriend = async () => {
      await addFriend();
      hideAddFriendModal();
    };

    const sendFriendRequestNotification = (receiverId) => {
      if (notificationSocket && notificationSocket.readyState === WebSocket.OPEN) {
        const message = `friend_request:You have a new friend request from User ${localStorage.getItem("loginId")}`;
        notificationSocket.send(message);
      }
    };

    // 新增：获取好友请求列表
    const fetchFriendRequests = async () => {
      try {
        const response = await api.getFriendRequests(localStorage.getItem("loginId"));
        friendRequests.value = response.data;
      } catch (error) {
        console.error('Error fetching friend requests:', error);
      }
    };

    onMounted(async () => {
      try {
        friends.value = (await api.getAcceptedFriends(localStorage.getItem("loginId"))).data;
        connectChatWebSocket();
        connectNotificationWebSocket();
        // 新增：获取好友请求列表
        fetchFriendRequests();
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
      searchResults,
      friendRequestNotification,
      acceptFriendRequestNotification,
      rejectFriendRequestNotification,
      friendRequests, // 新增：将 friendRequests 加入返回对象
      acceptFriendRequest,
      rejectFriendRequest,
      fetchFriendRequests // 新增：将 fetchFriendRequests 加入返回对象
    };
  }
};
</script>