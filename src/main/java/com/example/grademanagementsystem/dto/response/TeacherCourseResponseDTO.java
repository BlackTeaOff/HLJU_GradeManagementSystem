package com.example.grademanagementsystem.dto.response;

// 用来把该老师教授的课程返回给前端
public record TeacherCourseResponseDTO(int courseId, String courseName) {
}
