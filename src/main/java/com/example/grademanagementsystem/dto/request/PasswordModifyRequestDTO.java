package com.example.grademanagementsystem.dto.request;

public record PasswordModifyRequestDTO(
        String old_password,
        String new_password
) {
}
