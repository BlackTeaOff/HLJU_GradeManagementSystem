package com.example.grademanagementsystem.dto.requeset;

public record UserCreatedRequestDTO(
        String name,
        String password,
        String role
) {
}
