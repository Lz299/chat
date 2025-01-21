// FILEPATH: chat/chat/src/main/java/com/example/chat/config/WebSocketNotificationConfig.java
package com.example.chat.config;


import com.example.chat.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketNotificationConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler(), "/notification")
                .setAllowedOrigins("*");
    }

    @Bean
    public NotificationWebSocketHandler notificationWebSocketHandler() {
        return new NotificationWebSocketHandler(webSocketNotificationService);
    }
}