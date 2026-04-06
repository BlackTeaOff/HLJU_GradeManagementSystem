package com.example.grademanagementsystem.dto.request;

public record TeacherCreateRequestDTO(
        String name,
        String password,
        String role,
        String department
) {
}
