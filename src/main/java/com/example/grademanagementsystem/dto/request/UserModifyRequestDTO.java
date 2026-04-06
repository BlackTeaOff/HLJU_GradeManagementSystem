package com.example.grademanagementsystem.dto.request;
public record UserModifyRequestDTO(
        int id,
        String name,
        String role, // student, teacher, admin
        String department,
        String level,
        String enrollment_year,
        String major,
        String student_group,
        String password // optional
) {}
