package com.example.grademanagementsystem.config;

import com.example.grademanagementsystem.dto.request.AdminCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherCreateRequestDTO;
import com.example.grademanagementsystem.repository.UserBaseRepository;
import com.example.grademanagementsystem.service.CourseService;
import com.example.grademanagementsystem.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    // CommandLineRunner: Spring Boot应用启动完成后, 自动执行里面的代码, 适合初始化
    @Bean
    public CommandLineRunner initDefaultUsers(UserService userService, UserBaseRepository userBaseRepository, CourseService courseService) {
        return args -> { // CommandLineRunner里只有一个方法run(String... args), 可以直接用lambda表达式调用
            // 先判断数据库是不是空的，防止每次重启重复插入报错
            if (userBaseRepository.count() == 0) {
                // 初始化默认管理员
                userService.createAdmin(new AdminCreateRequestDTO("admin", "123456", "admin", "1"));
                
                // 初始化默认老师
                userService.createTeacher(new TeacherCreateRequestDTO("张老师", "123456", "teacher", "数学学院"));
                userService.createTeacher(new TeacherCreateRequestDTO("刘老师", "123456", "teacher", "计算机学院"));
                
                // 初始化默认学生
                userService.createStudent(new StudentCreateRequestDTO("小明", "123456", "student", "2023", "软工", "1班"));
                userService.createStudent(new StudentCreateRequestDTO("小坤", "123456", "student", "2024", "计算机", "4班"));

                courseService.createCourse(new CourseCreateRequestDTO("软件工程综合实践", List.of(2)));
                courseService.createCourse(new CourseCreateRequestDTO("计算机组成原理", List.of(3)));
                System.out.println("====== 默认用户初始化完成 ======");
            }
        };
    }
}
