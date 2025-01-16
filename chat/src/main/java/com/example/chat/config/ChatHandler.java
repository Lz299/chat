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
        if (session.getUri().getPath().equals("/chat")) {
            return;
        } else {
            String[] pathParts = session.getUri().getPath().split("/");
            // 确保路径部分的长度足够
            if (pathParts.length >= 4) {
                String senderIdStr = pathParts[2];
                String receiverIdStr = pathParts[3];
                int senderId = Integer.parseInt(senderIdStr);
                int receiverId = Integer.parseInt(receiverIdStr);
                // 存储用户会话信息
                userSessions.put(senderId, session.getId());
                // 存储接收者的会话信息，确保不同用户使用不同的 session
                WebSocketSession receiverSession = createReceiverSession(senderId, receiverId);
                if (receiverSession!= null) {
                    userSessions.put(receiverId, receiverSession.getId());
                } else {
                    System.out.println("Failed to create receiver session.");
                }
            } else {
                System.out.println("Invalid path format: " + session.getUri().getPath());
            }
        }
    }


    private WebSocketSession createReceiverSession(int senderId, int receiverId) {
        // 这里可以根据你的需求创建接收者的 WebSocketSession，例如通过某种查找或创建逻辑
        // 以下是一个简单示例，假设接收者的 session 已经存在，你可以根据实际情况修改
        // 可能需要根据你的业务逻辑和用户管理机制找到接收者的 session
        String receiverSessionId = userSessions.get(receiverId);
        if (receiverSessionId!= null) {
            return sessions.get(receiverSessionId);
        }
        return null;
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        // 从路径参数中获取 senderId 和 receiverId
        if (session.getUri().getPath().equals("/chat")) {
            return;
        } else {
            String[] pathParts = session.getUri().getPath().split("/");
            // 确保路径部分的长度足够
            if (pathParts.length >= 4) {
                String senderIdStr = pathParts[2];
                String receiverIdStr = pathParts[3];
                int senderId = Integer.parseInt(senderIdStr);
                int receiverId = Integer.parseInt(receiverIdStr);
                // 移除用户会话信息
                userSessions.remove(senderId);
                userSessions.remove(receiverId);
            } else {
                System.out.println("Invalid path format: " + session.getUri().getPath());
            }
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
        // 获取发送者和接收者的会话 ID
        String senderSessionId = userSessions.get(senderId);
        String receiverSessionId = userSessions.get(receiverId);


        if (senderSessionId!= null) {
            WebSocketSession senderSession = sessions.get(senderSessionId);
            if (senderSession!= null && senderSession.isOpen()) {
                try {
                    senderSession.sendMessage(new TextMessage(chatMessage.toJson()));
                } catch (IOException e) {
                    System.out.println("Error sending message to sender: " + e.getMessage());
                }
            } else {
                System.out.println("Sender session is null or closed.");
            }
        }


        if (receiverSessionId!= null) {
            WebSocketSession receiverSession = sessions.get(receiverSessionId);
            if (receiverSession!= null && receiverSession.isOpen()) {
                try {
                    receiverSession.sendMessage(new TextMessage(chatMessage.toJson()));
                } catch (IOException e) {
                    System.out.println("Error sending message to receiver: " + e.getMessage());
                }
            } else {
                System.out.println("Receiver session is null or closed.");
            }
        }
    }
}