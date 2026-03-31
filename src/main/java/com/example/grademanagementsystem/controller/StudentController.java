package com.example.grademanagementsystem.controller;

import com.example.grademanagementsystem.dto.response.GradeResponse;
import com.example.grademanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/{id}")
    public GradeResponse 查看成绩 (@PathVariable int id) {

    }
}
