package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.CourseCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseDeleteRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseModifyRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseStudentRequestDTO;
import com.example.grademanagementsystem.dto.response.CourseInfoResponseDTO;
import com.example.grademanagementsystem.dto.response.CourseStudentResponseDTO;
import com.example.grademanagementsystem.entity.CourseStudent;
import com.example.grademanagementsystem.repository.CourseStudentRepository;

import java.util.List;

public interface CourseService {
    Integer createCourse(CourseCreateRequestDTO courseCreateRequestDTO);

    void modifyCourse(CourseModifyRequestDTO courseModifyRequestDTO);

    void deleteCourse(CourseDeleteRequestDTO courseDeleteRequestDTO);

    List<CourseInfoResponseDTO> getAllCourses();

    List<CourseStudentResponseDTO> getAllStudents(CourseStudentRequestDTO courseStudentRequestDTO);
}
