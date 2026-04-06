package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.common.Result;
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

    @GetMapping("/{id}/grades") // RESTful规范, 一个学生下的信息
    public Result<List<StudentGradeResponseDTO>> queryGrades(@PathVariable int id) {
        return Result.success(studentService.queryGrades(new StudentGradeQueryRequestDTO(id)));
    }

    @PostMapping("{id}/courses/{courseId}/select")
    public Result<Void> selectCourse(@PathVariable int id, @PathVariable int courseId) {
        studentService.selectCourse(new CourseSelectRequestDTO(id, courseId));
        return Result.success();
    }

    @DeleteMapping("{id}/courses/{courseId}/drop")
    public Result<Void> dropCourse(@PathVariable int id, @PathVariable int courseId) {
        studentService.dropCourse(new CourseDropRequestDTO(id, courseId));
        return Result.success();
    }

    @GetMapping("/{id}/courses")
    public Result<List<StudentCourseResponseDTO>> queryCourses(@PathVariable int id) {
        return Result.success(studentService.queryCourses(new StudentCourseRequestDTO(id)));
    }
}
