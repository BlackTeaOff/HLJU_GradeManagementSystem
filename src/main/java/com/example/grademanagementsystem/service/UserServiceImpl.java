package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.UserCreateDTO;
import com.example.grademanagementsystem.entity.UserBase;
import com.example.grademanagementsystem.entity.UserStudent;
import com.example.grademanagementsystem.repository.UserBaseRepository;
import com.example.grademanagementsystem.repository.UserStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private UserStudentRepository userStudentRepository;

    @Override
    public void createUser(UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getRole().equals("student")) {
            UserStudent student = new UserStudent();
            student.setName(userCreateDTO.getName());
            student.setPassword(userCreateDTO.getPassword());
            student.setYear(userCreateDTO.getEnrollment_year());
            student.setMajor(userCreateDTO.getMajor());
            student.setGroup(userCreateDTO.getStudent_group());
            userStudentRepository.save(student);
        } else {
            UserBase user = new UserBase();
            user.setName(userCreateDTO.getName());
            user.setPassword(userCreateDTO.getPassword());
            userBaseRepository.save(user);
        }
    }
}
