package com.example.grademanagementsystem.dto.request;

import java.util.List;

public record CourseCreateRequestDTO(
        String name, // 不需要有id, 因为id是数据库自增生成的
        List<Integer> teacherIds // 从前端传入的是老师的id, 所以存teacherId列表
) {
}
