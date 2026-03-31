package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.dto.UserCreateDTO;
import com.example.grademanagementsystem.entity.UserBase;
import com.example.grademanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //@PostMapping("/user/create/")
    //public void createUser(@RequestBody UserCreateDTO userCreateDTO) {
       // userService.createUser(userCreateDTO);
    //}
}
