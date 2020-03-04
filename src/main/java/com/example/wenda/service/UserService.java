package com.example.wenda.service;


import com.example.wenda.mapper.UserMapper;
import com.example.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUser(int id){
        return userMapper.selectById(id);
    }
}
