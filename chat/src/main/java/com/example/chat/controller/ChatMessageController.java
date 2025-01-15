package com.example.chat.controller;


import com.example.chat.pojo.ChatMessage;
import com.example.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        return chatMessageService.saveMessage(message);
    }

    @GetMapping("/{senderId}/{receiverId}")
    public List<ChatMessage> getMessagesBetweenUsers(
            @PathVariable int senderId,
            @PathVariable int receiverId) {
        return chatMessageService.getMessagesBetweenUsers(senderId, receiverId);
    }
}