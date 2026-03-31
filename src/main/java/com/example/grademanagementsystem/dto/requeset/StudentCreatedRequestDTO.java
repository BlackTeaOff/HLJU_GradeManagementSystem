package com.example.grademanagementsystem.dto.requeset;

public record StudentCreatedRequestDTO(
        String name,
        String password,
        String role,
        String enrollment_year,
        String major,
        String student_group
) {
}
