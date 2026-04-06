package com.example.grademanagementsystem.dto.response;

public record TeacherGradeResponseDTO( // 老师查询成绩返回的数据
                                       int studentId,
                                       String studentName,
                                       int courseId,
                                       String courseName,
                                       Integer grade // 可能为null, 所以用Integer
) {}

