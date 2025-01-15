package com.example.chat.service.impl;


import com.example.chat.mapper.FriendshipMapper;
import com.example.chat.mapper.UserMapper;
import com.example.chat.pojo.User;
import com.example.chat.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipMapper friendshipMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void addFriendship(int user1Id, int user2Id) {
        friendshipMapper.addFriendship(user1Id, user2Id);
    }

    @Override
    public List<Integer> getFriendsByUserId(int userId) {
        return friendshipMapper.getFriendsByUserId(userId);
    }

    @Override
    public boolean isFriendshipAccepted(int user1Id, int user2Id) {
        return friendshipMapper.isFriendshipAccepted(user1Id, user2Id);
    }

    @Override
    public void acceptFriendship(int user1Id, int user2Id) {
        friendshipMapper.acceptFriendship(user1Id, user2Id);
        // 更新 Redis 中的好友状态
        String key = "user:" + user1Id + ":friends";
        if (redisTemplate.opsForSet().isMember(key, String.valueOf(user2Id))) {
            redisTemplate.opsForSet().add(key, String.valueOf(user2Id));
        }
        key = "user:" + user2Id + ":friends";
        if (redisTemplate.opsForSet().isMember(key, String.valueOf(user1Id))) {
            redisTemplate.opsForSet().add(key, String.valueOf(user1Id));
        }
    }

    @Override
    public List<User> getAcceptedFriends(int userId) {
        List<Integer> friendIds = friendshipMapper.getFriendsByUserId(userId);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendIds) {
            friends.add(userMapper.findById(friendId));
        }
        return friends;
    }
}