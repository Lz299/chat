// FILEPATH: chat/chat/src/main/java/com/example/chat/service/FriendshipNotificationConsumer.java
package com.example.chat.service;


import com.alibaba.fastjson2.JSON;
import com.example.chat.pojo.User;
import com.example.chat.service.WebSocketNotificationService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FriendshipNotificationConsumer {

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
@Autowired
private UserService userService;
    @RabbitListener(queues = "friend_request_queue")
    public void receiveMessage(String message) {
        // 确保键的类型是 String，值的类型是 Object
//        String uid1 = (String) map.get("user1Id");
//        String uid2 = (String) map.get("user2Id");

        String[] parts = message.split(":");
        if (parts.length == 2) {
            try {
                int user1Id = Integer.parseInt(parts[0]);
                int user2Id = Integer.parseInt(parts[1]);

               User user= userService.findUserById(user1Id);



        // 发送通知消息到接收者的 WebSocket 会话
        webSocketNotificationService.sendNotification(user2Id, JSON.toJSONString(user));
            } catch (NumberFormatException e) {
                // 处理解析异常
                e.printStackTrace();
            }
        } else {
            // 处理消息格式异常
            System.err.println("Invalid message format: " + message);
        }
    }


}