<template>
  <div>
    <h2>Chat with {{ receiverId }}</h2>
    <div v-for="(msg, index) in messages" :key="index">{{ msg.message }}</div>
    <input v-model="inputMessage" @keyup.enter="sendMessage" />
  </div>
</template>


<script>
import { ref, onMounted } from 'vue';
import api from '../service/api';
import { useRoute } from 'vue-router';


export default {
  setup() {
    const route = useRoute();
    const senderId = parseInt(route.params.senderId);
    const receiverId = parseInt(route.params.receiverId);
    const messages = ref([]);
    const inputMessage = ref('');
    let socket;


    const connectWebSocket = () => {
      socket = new WebSocket(`ws://localhost:8089/chat/${senderId}/${receiverId}`);
      socket.onopen = () => {
        console.log('WebSocket connected');
      };
      socket.onmessage = (event) => {
        console.log(event)
        try {
          const chatMessage = JSON.parse(event.data);
          // 只添加与当前聊天相关的消息
          if ((chatMessage.sender_id === senderId && chatMessage.receiver_id === receiverId) ||
              (chatMessage.sender_id === receiverId && chatMessage.receiver_id === senderId)) {
            if (Array.isArray(messages.value)) {
              messages.value.push(chatMessage);
            } else {
              console.error('messages.value is not an array.');
            }
          }
        } catch (e) {
          console.error('Error parsing chat message:', e);
        }
      };
      socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
      socket.onclose = () => {
        console.log('WebSocket closed');
      };
    };


    const sendMessage = async () => {
      if (inputMessage.value && socket) {
        try {
          const message = {
            sender_id: senderId,
            receiver_id: receiverId,
            message: inputMessage.value
          };
          await api.sendMessage(message);
          socket.send(JSON.stringify(message));
          inputMessage.value = '';
        } catch (error) {
          console.error('Error sending message:', error);
        }
      } else {
        console.error('Socket is not connected.');
      }
    };


    onMounted(async () => {
      try {
        // 获取用户之间的消息
        const fetchedMessages = await api.getMessagesBetweenUsers(senderId, receiverId);
        if (Array.isArray(fetchedMessages.data)) {
          messages.value = fetchedMessages.data;
        } else {
          console.error('Fetched messages are not an array.');
        }
        connectWebSocket();
      } catch (error) {
        console.error('Error fetching messages:', error);
      }
    });


    return {
      senderId,
      receiverId,
      messages,
      inputMessage,
      sendMessage
    };
  }
};
</script>