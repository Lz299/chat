package com.example.chat.service.impl;


import com.example.chat.mapper.UserMapper;

import com.example.chat.pojo.User;
import com.example.chat.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User registerUser(User user) {
        user.setPassword(user.getPassword());
        userMapper.insertUser(user);
        return user;
    }

    @Override
    public User findUserById(int id) {
        return userMapper.findById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public int authenticateUser(String username, String password) {
        User user = findUserByUsername(username);
        if (user == null) {
            return 0;
        }

        if (password.equals(user.getPassword())){
            return user.getId();
        }
        return 0;
    }
}