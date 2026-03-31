package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.UserCreateDTO;
import com.example.grademanagementsystem.entity.UserBase;
import org.springframework.stereotype.Service;

public interface UserService {
    //注册
    //登录
    //修改密码
    //注销
    void createUser(UserCreateDTO userCreateDTO);
}
