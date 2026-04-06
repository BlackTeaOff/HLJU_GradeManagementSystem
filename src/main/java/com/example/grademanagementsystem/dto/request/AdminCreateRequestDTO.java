package com.example.grademanagementsystem.dto.request;

public record AdminCreateRequestDTO(
        String name,
        String password,
        String role,
        String level
) {
}
