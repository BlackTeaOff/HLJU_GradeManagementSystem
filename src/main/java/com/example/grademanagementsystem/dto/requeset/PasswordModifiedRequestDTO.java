package com.example.grademanagementsystem.dto.requeset;

public record PasswordModifiedRequestDTO(
        int id,
        String old_password,
        String new_password
) {
}
