package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.common.Result;
import com.example.grademanagementsystem.dto.request.GradeInputRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherCourseRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherGradeQueryRequestDTO;
import com.example.grademanagementsystem.dto.response.TeacherCourseResponseDTO;
import com.example.grademanagementsystem.dto.response.TeacherGradeResponseDTO;
import com.example.grademanagementsystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping("{id}/{courseId}/grades")
    public Result<List<TeacherGradeResponseDTO>> queryGrades(@PathVariable int id, @PathVariable int courseId) {
        return Result.success(teacherService.queryGrades(new TeacherGradeQueryRequestDTO(id, courseId)));
    }

    @PostMapping("/grades") // 录入成绩(创建用POST), 符合RESTful规范, 只用名词不用动词
    public Result<Void> inputGrade(@RequestBody GradeInputRequestDTO gradeInputRequestDTO) {
        teacherService.inputGrade(gradeInputRequestDTO);
        return Result.success();
    }

    @PutMapping("/grades") // 修改成绩(更新用PUT), 根据前端的请求方式来确定是哪个
    public Result<Void> modifyGrade(@RequestBody GradeInputRequestDTO gradeInputRequestDTO) {
        teacherService.modifyGrade(gradeInputRequestDTO);
        return Result.success();
    }

    // 老师查看自己所教的课程
    @GetMapping("/{id}/courses")
    public Result<List<TeacherCourseResponseDTO>> queryCourses(@PathVariable int id) {
        return Result.success(teacherService.queryCourses(new TeacherCourseRequestDTO(id)));
    }
}
