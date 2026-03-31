package com.example.grademanagementsystem.dto.requeset;

public record AdminCreatedRequestDTO(
        String name,
        String password,
        String role,
        String level
) {
}
