package com.example.chat.service;


import com.example.chat.pojo.User;

public interface UserService {
    User registerUser(User user);
    User findUserById(int id);
    User findUserByUsername(String username);
    int authenticateUser(String username, String password);
}