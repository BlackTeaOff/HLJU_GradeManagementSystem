package com.example.grademanagementsystem.dto.requeset;

import com.example.grademanagementsystem.entity.UserTeacher;

import java.util.List;

public record CourseCreatedRequestDTO(
        int course_id,
        String name,
        List<UserTeacher> teachers
) {
}
