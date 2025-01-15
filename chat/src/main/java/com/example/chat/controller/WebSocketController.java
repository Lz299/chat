package com.example.chat.controller;


import com.example.chat.config.ChatHandler;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.standard.SpringConfigurator;




@ServerEndpoint(value = "/chat/{senderId}/{receiverId}", configurator = SpringConfigurator.class)
@RestController
@RequestMapping("/chat")
public class WebSocketController {


    @GetMapping("/connect/{senderId}/{receiverId}")
    public void connectWebSocket(@PathVariable int senderId, @PathVariable int receiverId, HttpSession session) {
        session.setAttribute("senderId", senderId);
        session.setAttribute("receiverId", receiverId);
    }


    // 其他可能的方法和逻辑
}


