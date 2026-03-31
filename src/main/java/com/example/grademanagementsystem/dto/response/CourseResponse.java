package com.example.grademanagementsystem.dto.response;

import com.example.grademanagementsystem.entity.UserTeacher;

import java.util.List;

public record CourseResponse(
        int course_id,
        String name,
        List<UserTeacher> teachers
) {
}
