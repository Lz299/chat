package com.example.chat.controller;

import com.example.chat.config.RabbitMQConfig;
import com.example.chat.pojo.FriendRequest;
import com.example.chat.pojo.User;
import com.example.chat.service.FriendshipService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @GetMapping("/{userId}")
    public List<Integer> getFriendsByUserId(@PathVariable int userId) {
        return friendshipService.getFriendsByUserId(userId);
    }



    @GetMapping("/search")
    public List<User> searchFriends(@RequestParam String keyword) {
        return friendshipService.searchFriends(keyword);
    }

    @PostMapping("/add")
    public void addFriendship(@RequestParam int user1Id, @RequestParam int user2Id) {
        // 调用 friendshipService 等逻辑
        String message = user1Id + ":" + user2Id;
        friendshipService.addFriendship(user1Id, user2Id);
        rabbitTemplate.convertAndSend(RabbitMQConfig.FRIEND_REQUEST_EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);

    }

    @PostMapping("/accept")
    public void acceptFriendship(@RequestParam int user1Id, @RequestParam int user2Id) {
        friendshipService.acceptFriendship(user1Id, user2Id);
    }

    @PostMapping("/reject")
    public void rejectFriendship(@RequestParam int user1Id, @RequestParam int user2Id) {
        friendshipService.rejectFriendship(user1Id, user2Id);
    }

    @GetMapping("/requests/{userId}")
    public List<User> getFriendRequests(@PathVariable int userId) {

        return friendshipService.getFriendRequests(userId);
    }





}