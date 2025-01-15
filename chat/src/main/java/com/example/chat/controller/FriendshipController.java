package com.example.chat.controller;



import com.example.chat.service.FriendshipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/add")
    public void addFriendship(@RequestParam int user1Id, @RequestParam int user2Id) {
        friendshipService.addFriendship(user1Id, user2Id);
    }

    @GetMapping("/{userId}")
    public List<Integer> getFriendsByUserId(@PathVariable int userId) {
        return friendshipService.getFriendsByUserId(userId);
    }

    @PostMapping("/accept")
    public void acceptFriendship(@RequestParam int user1Id, @RequestParam int user2Id, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        friendshipService.acceptFriendship(user1Id, user2Id);
    }
}