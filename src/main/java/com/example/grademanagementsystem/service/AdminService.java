package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.requeset.CourseCreatedRequestDTO;
import com.example.grademanagementsystem.dto.requeset.CourseDeletedRequestDTO;
import com.example.grademanagementsystem.dto.requeset.UserDeletedRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    //删除用户
    void deleteUser(UserDeletedRequestDTO userDeletedRequestDTO);
    //修改课程
    void addCourse(CourseCreatedRequestDTO courseCreatedRequestDTO);
    void modifyCourse(CourseCreatedRequestDTO courseCreatedRequestDTO);
    //删除课程
    void deleteCourse(CourseDeletedRequestDTO courseDeletedRequestDTO);
}
