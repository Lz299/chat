package com.example.chat.config;


import com.example.chat.pojo.ChatMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ChatHandler extends TextWebSocketHandler {
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Map<Integer, String> userSessions = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        // 从路径参数中获取 senderId 和 receiverId
        if (session.getUri().getPath().equals("/chat")){
            return;
        }else {
            String senderIdStr = session.getUri().getPath().split("/")[2];
            String receiverIdStr = session.getUri().getPath().split("/")[3];
            int senderId = Integer.parseInt(senderIdStr);
            int receiverId = Integer.parseInt(receiverIdStr);
            // 存储用户会话信息
            userSessions.put(senderId, session.getId());
            userSessions.put(receiverId, session.getId());
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        // 从路径参数中获取 senderId 和 receiverId

        if (session.getUri().getPath().equals("/chat")){
            return;
        }else {
        String senderIdStr = session.getUri().getPath().split("/")[2];
        String receiverIdStr = session.getUri().getPath().split("/")[3];
        int senderId = Integer.parseInt(senderIdStr);
        int receiverId = Integer.parseInt(receiverIdStr);
        // 移除用户会话信息
        userSessions.remove(senderId);
        userSessions.remove(receiverId);
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        try {
            chatMessage = ChatMessage.fromJson(message.getPayload());
        } catch (Exception e) {
            System.out.println("Error parsing chat message: " + e.getMessage());
            return;
        }
        // 获取发送者和接收者的 ID
        int senderId = chatMessage.getSender_id();
        int receiverId = chatMessage.getReceiver_id();
        // 获取接收者的会话 ID
        String receiverSessionId = userSessions.get(receiverId);
        if (receiverSessionId!= null) {
            WebSocketSession receiverSession = sessions.get(receiverSessionId);
            if (receiverSession!= null && receiverSession.isOpen()) {
                receiverSession.sendMessage(message);
            }
        }
    }
}