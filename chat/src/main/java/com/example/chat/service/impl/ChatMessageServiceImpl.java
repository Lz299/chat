package com.example.chat.service.impl;


import com.example.chat.mapper.ChatMessageMapper;
import com.example.chat.pojo.ChatMessage;
import com.example.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public ChatMessage saveMessage(ChatMessage message) {
        chatMessageMapper.insertMessage(message);
        return message;
    }

    @Override
    public List<ChatMessage> getMessagesBetweenUsers(int senderId, int receiverId) {
        return chatMessageMapper.getMessagesBetweenUsers(senderId, receiverId);
    }
}