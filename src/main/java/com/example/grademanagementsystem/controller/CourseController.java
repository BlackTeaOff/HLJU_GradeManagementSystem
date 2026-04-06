package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.common.Result;
import com.example.grademanagementsystem.dto.request.CourseCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseDeleteRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseModifyRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseStudentRequestDTO;
import com.example.grademanagementsystem.dto.response.CourseInfoResponseDTO;
import com.example.grademanagementsystem.dto.response.CourseStudentResponseDTO;
import com.example.grademanagementsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public Result<Integer> createCourse(@RequestBody CourseCreateRequestDTO courseCreateRequestDTO) {
        Integer courseId = courseService.createCourse(courseCreateRequestDTO);
        return Result.success(courseId);
    }

    @PutMapping("/modify") // 更新资源用Put
    public Result<Void> modifyCourse(@RequestBody CourseModifyRequestDTO courseModifyRequestDTO) {
        courseService.modifyCourse(courseModifyRequestDTO);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteCourse(@RequestBody CourseDeleteRequestDTO courseDeleteRequestDTO) {
        courseService.deleteCourse(courseDeleteRequestDTO);
        return Result.success();
    }

    @GetMapping // 返回所有课程的接口, 用来给学生选课用
    public Result<List<CourseInfoResponseDTO>> getAllCourses() {
        return Result.success(courseService.getAllCourses());
    }

    @GetMapping("/{courseId}/students") // 返回课程所有学生的接口
    public Result<List<CourseStudentResponseDTO>> getAllStudents(@PathVariable int courseId) {
        return Result.success(courseService.getAllStudents(new CourseStudentRequestDTO(courseId)));
    }
}
