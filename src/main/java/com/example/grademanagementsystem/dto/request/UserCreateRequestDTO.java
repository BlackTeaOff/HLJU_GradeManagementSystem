package com.example.grademanagementsystem.dto.request;

public record UserCreateRequestDTO(
        String name,
        String password,
        String role
) {
}
