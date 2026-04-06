package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.*;
import org.thymeleaf.util.ObjectUtils;

import java.util.List;

public interface UserService {
    //注册
    //登录
    //修改密码
    //注销
    //void createUser(UserCreateDTO userCreateDTO);
    Integer createStudent(StudentCreateRequestDTO studentCreateRequestDTO); // create把创建的ID返回
    Integer createTeacher(TeacherCreateRequestDTO teacherCreateRequestDTO);
    Integer createAdmin(AdminCreateRequestDTO adminCreateRequestDTO);

    String login(UserLoginDTO userLoginDTO);

    void deleteUser(UserDeleteRequestDTO userDeleteRequestDTO);
    
    void modifyUser(UserModifyRequestDTO userModifyRequestDTO);

    void modifyPassword(int userId, PasswordModifyRequestDTO passwordModifyRequestDTO);

    Object queryInfo(InfoQueryRequestDTO infoQueryRequestDTO);

    List<Object> queryAllUsers();
}
