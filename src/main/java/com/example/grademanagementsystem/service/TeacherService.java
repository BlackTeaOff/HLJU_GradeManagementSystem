package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.requeset.GradeAddedRequestDTO;
import com.example.grademanagementsystem.dto.requeset.GradeCheckedRequestDTO;
import com.example.grademanagementsystem.dto.response.GradeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TeacherService {
    //查看成绩
    //录入成绩
    //修改成绩
    GradeResponse checkedGrade(GradeCheckedRequestDTO gradeCheckedRequestDTO);
    void addGrade(GradeAddedRequestDTO gradeAddedRequestDTO);
    void modifyGrade(GradeAddedRequestDTO gradeAddedRequestDTO);
}
