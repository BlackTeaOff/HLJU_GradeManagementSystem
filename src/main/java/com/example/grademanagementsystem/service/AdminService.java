package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.CourseCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseDeleteRequestDTO;
import com.example.grademanagementsystem.dto.request.UserDeleteRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    //删除用户
    void deleteUser(UserDeleteRequestDTO userDeleteRequestDTO);
    //修改课程
    void addCourse(CourseCreateRequestDTO courseCreateRequestDTO);
    void modifyCourse(CourseCreateRequestDTO courseCreateRequestDTO);
    //删除课程
    void deleteCourse(CourseDeleteRequestDTO courseDeleteRequestDTO);
}
