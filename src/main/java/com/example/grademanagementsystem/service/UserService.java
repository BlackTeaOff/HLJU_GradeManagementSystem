package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.UserCreateDTO;
import com.example.grademanagementsystem.entity.UserBase;
import org.springframework.stereotype.Service;

public interface UserService {
    void createUser(UserCreateDTO userCreateDTO);
}
