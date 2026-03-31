package com.example.grademanagementsystem.dto.requeset;

public record GradeAddedRequestDTO(
        int student_id,
        int course_id,
        int score
) {
}
