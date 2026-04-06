package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.GradeInputRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherCourseRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherGradeQueryRequestDTO;
import com.example.grademanagementsystem.dto.response.TeacherCourseResponseDTO;
import com.example.grademanagementsystem.dto.response.TeacherGradeResponseDTO;

import java.util.List;

public interface TeacherService {
    //查看成绩
    //录入成绩
    //修改成绩
    // List<TeacherGradeResponseDTO> queryGrades(GradeQueryRequestDTO gradeQueryRequestDTO);
    List<TeacherGradeResponseDTO> queryGrades(TeacherGradeQueryRequestDTO teacherGradeQueryRequestDTO);

    void inputGrade(GradeInputRequestDTO gradeInputRequestDTO);

    void modifyGrade(GradeInputRequestDTO gradeInputRequestDTO);

    List<TeacherCourseResponseDTO> queryCourses(TeacherCourseRequestDTO teacherCourseRequestDTO);
}
