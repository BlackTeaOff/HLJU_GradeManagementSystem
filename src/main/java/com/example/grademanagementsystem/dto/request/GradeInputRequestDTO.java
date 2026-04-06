package com.example.grademanagementsystem.dto.request;

public record GradeInputRequestDTO(
        Integer studentId,
        Integer courseId,
        Integer grade
) {}
