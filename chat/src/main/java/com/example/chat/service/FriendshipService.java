package com.example.chat.service;

import com.example.chat.pojo.User;

import java.util.List;
import java.util.List;

public interface FriendshipService {
    void addFriendship(int user1Id, int user2Id);
    List<Integer> getFriendsByUserId(int userId);
    boolean isFriendshipAccepted(int user1Id, int user2Id);
    void acceptFriendship(int user1Id, int user2Id);
    List<User> getAcceptedFriends(int userId);
    List<User> searchFriends(String keyword);
}