package com.example.grademanagementsystem.dto.request;

public record StudentCreateRequestDTO(
        String name,
        String password,
        String role,
        String enrollment_year,
        String major,
        String student_group
) {
}
