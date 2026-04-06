package com.example.grademanagementsystem.dto.response;

public record StudentGradeResponseDTO( // 一个学生的课程和其分数, 返回前端这个的对象数组(用来学生查询成绩)
                                       int courseId,
                                       String courseName,
                                       Integer grade
) {}
