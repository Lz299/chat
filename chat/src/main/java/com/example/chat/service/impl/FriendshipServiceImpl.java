package com.example.chat.service.impl;


import com.example.chat.mapper.FriendshipMapper;
import com.example.chat.mapper.UserMapper;
import com.example.chat.pojo.FriendRequest;
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
    }

    @Override
    public List<User> getAcceptedFriends(int userId) {
        List<Integer> friendIds = friendshipMapper.getFriendsByUserId(userId);
        return userMapper.getUsersByIds(friendIds);
    }

    @Override
    public List<User> searchFriends(String keyword) {
        return userMapper.searchUsers(keyword);
    }
}