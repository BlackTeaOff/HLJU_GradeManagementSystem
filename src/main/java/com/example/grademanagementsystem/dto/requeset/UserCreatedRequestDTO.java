package com.example.grademanagementsystem.dto.requeset;

public record UserCreatedRequestDTO(
        int id,
        String name,
        String password,
        String grade
) {
}
