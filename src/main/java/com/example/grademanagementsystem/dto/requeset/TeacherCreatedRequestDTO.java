package com.example.grademanagementsystem.dto.requeset;

public record TeacherCreatedRequestDTO(
        String name,
        String password,
        String role,
        String department
) {
}
