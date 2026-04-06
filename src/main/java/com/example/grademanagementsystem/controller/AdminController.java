package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.common.Result;
import com.example.grademanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @Autowired
    UserService userService;

    // 管理员查看所有用户列表, 不知道是什么类型, 所以返回Object, 不耽误前端收到json字符串
    @GetMapping("/users")
    public Result<List<Object>> queryAllUsers() {
        return Result.success(userService.queryAllUsers());
    }
}
