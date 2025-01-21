package com.example.chat.config;// FILEPATH: chat/chat/src/main/java/com/example/chat/handler/NotificationWebSocketHandler.java


import com.example.chat.service.WebSocketNotificationService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketNotificationService webSocketNotificationService;

    public NotificationWebSocketHandler(WebSocketNotificationService webSocketNotificationService) {
        this.webSocketNotificationService = webSocketNotificationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 假设你可以从 session 中获取用户 ID，这里仅为示例，实际应用中可能需要解析 token 或其他方式
        int userId = getUserIdFromSession(session);
        webSocketNotificationService.addSession(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 可以添加处理客户端消息的逻辑
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 假设你可以从 session 中获取用户 ID，这里仅为示例，实际应用中可能需要解析 token 或其他方式
        int userId = getUserIdFromSession(session);
        webSocketNotificationService.removeSession(userId);
    }

    private int getUserIdFromSession(WebSocketSession session) {
        URI uri = session.getUri();
        String query = uri.getQuery();
        if (query!= null) {
            Pattern pattern = Pattern.compile("userId=(\\d+)");
            Matcher matcher = pattern.matcher(query);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return 0;

}
}