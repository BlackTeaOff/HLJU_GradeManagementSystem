package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.common.Result;
import com.example.grademanagementsystem.dto.request.*;
import com.example.grademanagementsystem.entity.UserBase;
import com.example.grademanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users") // 统一规范, 使用 /api/v1 前缀和复数名词, 表示不是返回html界面而是返回json数据
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/student") // 该方法的地址, 与全局地址合并为"/user/student"
    public Result<Integer> createStudent(@RequestBody StudentCreateRequestDTO request) {
        Integer id = userService.createStudent(request);
        return Result.success(id); // @RequestBody将前端json变为DTO对象
    }

    @PostMapping("/teacher")
    public Result<Integer> createTeacher(@RequestBody TeacherCreateRequestDTO request) {
        Integer id = userService.createTeacher(request);
        return Result.success(id);
    }

    @PostMapping("/admin")
    public Result<Integer> createAdmin(@RequestBody AdminCreateRequestDTO request) {
        Integer id = userService.createAdmin(request);
        return Result.success(id);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO request) {
        String token = userService.login(request);
        return Result.success(token);
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteUser(@RequestBody UserDeleteRequestDTO userDeleteRequestDTO) {
        userService.deleteUser(userDeleteRequestDTO);
        return Result.success();
    }

    @PostMapping("/modifyPassword")
    public Result<Void> modifyPassword(@RequestBody PasswordModifyRequestDTO passwordModifyRequestDTO) {
        userService.modifyPassword(passwordModifyRequestDTO);
        return Result.success();
    }

    // 查询个人信息接口
    @GetMapping("{id}/info")
    public Result<Object> queryInfo(@PathVariable int id) { // json序列化关心对象的实际属性, 可以用Object
        return Result.success(userService.queryInfo(new InfoQueryRequestDTO(id)));
    }
}
