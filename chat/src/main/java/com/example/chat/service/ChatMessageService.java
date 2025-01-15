package com.example.chat.service;




import com.example.chat.pojo.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage message);
    List<ChatMessage> getMessagesBetweenUsers(int senderId, int receiverId);
}