// FILEPATH: chat/chat/src/main/java/com/example/chat/service/WebSocketNotificationService.java
package com.example.chat.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebSocketNotificationService {
    private final Map<Integer, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    public void addSession(int userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    public void removeSession(int userId) {
        userSessions.remove(userId);
    }

    public void sendNotification(int receiverId, String notificationMessage) {
        WebSocketSession session = userSessions.get(receiverId);
        if (session!= null && session.isOpen()) {
            try {
                session.sendMessage(new org.springframework.web.socket.TextMessage(notificationMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}