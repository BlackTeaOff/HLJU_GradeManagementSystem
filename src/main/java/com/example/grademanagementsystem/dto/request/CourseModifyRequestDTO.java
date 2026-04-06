package com.example.grademanagementsystem.dto.request;

import java.util.List;

public record CourseModifyRequestDTO(
        int id, // 要修改课程的id
        String name,
        List<Integer> teacherIds
) {
}
