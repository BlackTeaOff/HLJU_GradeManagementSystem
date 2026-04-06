package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.CourseDropRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseSelectRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentCourseRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentGradeQueryRequestDTO;
import com.example.grademanagementsystem.dto.response.StudentCourseResponseDTO;
import com.example.grademanagementsystem.dto.response.StudentGradeResponseDTO;

import java.util.List;

public interface StudentService {
    //查看成绩
    List<StudentGradeResponseDTO> queryGrades(StudentGradeQueryRequestDTO studentGradeQueryRequestDTO);

    void selectCourse(CourseSelectRequestDTO courseSelectRequestDTO);

    // 退课
    void dropCourse(CourseDropRequestDTO dto);

    // 学生查看自己选的课程
    List<StudentCourseResponseDTO> queryCourses(StudentCourseRequestDTO studentCourseRequestDTO);
}
