package com.example.grademanagementsystem.dto;

import lombok.Data;

@Data
public class UserCreateDTO {

    private String name;

    private String password;

    private String role;

    private String enrollment_year;

    private String major;

    private String student_group;
}
