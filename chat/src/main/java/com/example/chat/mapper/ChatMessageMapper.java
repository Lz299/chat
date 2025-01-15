package com.example.chat.mapper;



import com.example.chat.pojo.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    int insertMessage(ChatMessage message);
    List<ChatMessage> getMessagesBetweenUsers(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
}