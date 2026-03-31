package com.example.grademanagementsystem.dto.response;

public record StudentResponse(
        String name,
        String role,
        String enrollment_year,
        String major,
        String student_group
) {
}
