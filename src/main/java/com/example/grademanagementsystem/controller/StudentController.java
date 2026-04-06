package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.common.Result;
import com.example.grademanagementsystem.common.UserContext;
import com.example.grademanagementsystem.dto.request.CourseDropRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseSelectRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentCourseRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentGradeQueryRequestDTO;
import com.example.grademanagementsystem.dto.response.StudentCourseResponseDTO;
import com.example.grademanagementsystem.dto.response.StudentGradeResponseDTO;
import com.example.grademanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    // Token里包含id, 不需要传ID了
    @GetMapping("/my/grades") // RESTful规范, 一个学生下的信息
    public Result<List<StudentGradeResponseDTO>> queryGrades() {
        int studentId = UserContext.getUserId(); // 从UserContext里直接拿
        return Result.success(studentService.queryGrades(new StudentGradeQueryRequestDTO(studentId)));
    }

    @PostMapping("/courses/{courseId}/select")
    public Result<Void> selectCourse(@PathVariable int courseId) {
        int studentId = UserContext.getUserId();
        studentService.selectCourse(new CourseSelectRequestDTO(studentId, courseId));
        return Result.success();
    }

    @DeleteMapping("/courses/{courseId}/drop")
    public Result<Void> dropCourse(@PathVariable int courseId) {
        int studentId = UserContext.getUserId();
        studentService.dropCourse(new CourseDropRequestDTO(studentId, courseId));
        return Result.success();
    }

    @GetMapping("/my/courses")
    public Result<List<StudentCourseResponseDTO>> queryCourses() {
        int studentId = UserContext.getUserId();
        return Result.success(studentService.queryCourses(new StudentCourseRequestDTO(studentId)));
    }
}
