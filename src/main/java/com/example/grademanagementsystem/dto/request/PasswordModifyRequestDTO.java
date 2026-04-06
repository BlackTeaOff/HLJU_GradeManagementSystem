package com.example.grademanagementsystem.dto.request;

public record PasswordModifyRequestDTO(
        int id,
        String old_password,
        String new_password
) {
}
