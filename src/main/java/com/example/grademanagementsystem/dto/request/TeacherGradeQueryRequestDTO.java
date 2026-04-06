package com.example.grademanagementsystem.dto.request;

public record TeacherGradeQueryRequestDTO(int teacherId, int courseId) { // 老师查成绩一般是在某门课程里查
}
