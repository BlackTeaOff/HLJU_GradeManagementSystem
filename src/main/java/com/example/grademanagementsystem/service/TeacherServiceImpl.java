package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.requeset.GradeAddedRequestDTO;
import com.example.grademanagementsystem.dto.requeset.GradeCheckedRequestDTO;
import com.example.grademanagementsystem.dto.response.GradeResponse;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService{

    @Override
    public GradeResponse checkedGrade(GradeCheckedRequestDTO gradeCheckedRequestDTO) {
        return null;
    }

    @Override
    public void addGrade(GradeAddedRequestDTO gradeAddedRequestDTO) {

    }

    @Override
    public void modifyGrade(GradeAddedRequestDTO gradeAddedRequestDTO) {

    }
}
