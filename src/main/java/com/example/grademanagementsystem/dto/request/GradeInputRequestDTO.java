package com.example.grademanagementsystem.dto.request;

public record GradeInputRequestDTO(
        Integer teacherId,
        Integer studentId,
        Integer courseId,
        Integer grade
) {}
