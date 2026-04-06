package com.example.grademanagementsystem.dto.response;

import java.util.List;

// 返回课程各种信息(选课用)
public record CourseInfoResponseDTO(int courseId, String courseName, List<String> teacherNames) { //
}
