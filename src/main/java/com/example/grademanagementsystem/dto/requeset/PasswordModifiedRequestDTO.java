package com.example.grademanagementsystem.dto.requeset;

public record PasswordModifiedRequestDTO(
        int id,
        int old_password,
        int new_password
) {
}
