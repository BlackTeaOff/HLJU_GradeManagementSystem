package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.CourseDropRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseSelectRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentCourseRequestDTO;
import com.example.grademanagementsystem.dto.request.StudentGradeQueryRequestDTO;
import com.example.grademanagementsystem.dto.response.StudentCourseResponseDTO;
import com.example.grademanagementsystem.dto.response.StudentGradeResponseDTO;
import com.example.grademanagementsystem.entity.Course;
import com.example.grademanagementsystem.entity.CourseStudent;
import com.example.grademanagementsystem.entity.UserStudent;
import com.example.grademanagementsystem.repository.CourseRepository;
import com.example.grademanagementsystem.repository.CourseStudentRepository;
import com.example.grademanagementsystem.repository.UserStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    CourseStudentRepository courseStudentRepository;

    @Autowired
    UserStudentRepository userStudentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public List<StudentGradeResponseDTO> queryGrades(StudentGradeQueryRequestDTO studentGradeQueryRequestDTO) {
        List<CourseStudent> courseStudents = courseStudentRepository.findAllByStudent_Id(studentGradeQueryRequestDTO.id());

        // 创建一个空的返回列表(返回给Controller), 里面是课程和成绩(学生查看成绩)
        List<StudentGradeResponseDTO> result = new ArrayList<>();

        // 因为查看成绩和查看课程在同一页面, 查看课程返回的信息有课程信息, 查看课程用查看成绩代替, 无成绩就是null
        for (CourseStudent cs : courseStudents) { // 遍历courseStudents数组里的每一条
                StudentGradeResponseDTO dto = new StudentGradeResponseDTO(
                        cs.getCourse().getId(),
                        cs.getCourse().getName(),
                        cs.getGrade()
                );
                result.add(dto); // 把这个dto放入要返回的dto列表里
            }
        return result;
    }

    @Override
    public void selectCourse(CourseSelectRequestDTO dto) {
        int studentId = dto.studentId();
        int courseId = dto.courseId();
        
        // 判断学生存不存在
        UserStudent student = userStudentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("该学生不存在！")); // 返回的是Optional, 可能不存在, 不存在就会throw
        // 判断课程存不存在
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("该课程不存在"));

        boolean isSelected = courseStudentRepository.existsByCourse_IdAndStudent_Id(courseId, studentId);
        if (isSelected) {
            throw new RuntimeException("你已选过该课程！");
        }

        CourseStudent courseStudent = new CourseStudent(); // 把该课程和学生关联
        courseStudent.setStudent(student);
        courseStudent.setCourse(course);
        courseStudentRepository.save(courseStudent);
    }

    // 退课
    @Override
    public void dropCourse(CourseDropRequestDTO dto) {
        int studentId = dto.studentId();
        int courseId = dto.courseId();

        // 只需要查询一次数据库记录是否存在即可
        CourseStudent courseStudent = courseStudentRepository.findByStudent_IdAndCourse_Id(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("你未选过该课程！无法退课。"));

        courseStudentRepository.delete(courseStudent);
    }

    @Override
    // 学生查看自己选的课程
    public List<StudentCourseResponseDTO> queryCourses(StudentCourseRequestDTO studentCourseRequestDTO) {
        // 用student查courseStudent表, 得到该学生的选课记录(CourseStudent), 就能找到course
        List<CourseStudent> courseStudents = courseStudentRepository.findAllByStudent_Id(studentCourseRequestDTO.studentId());

        List<StudentCourseResponseDTO> result = new ArrayList<>(); // 返回数组

        // 把courseStudent数组的每一条courseStudent拿出来, 依次打包成dto, 添加到数组返回
        for (CourseStudent cs : courseStudents) {
            StudentCourseResponseDTO dto = new StudentCourseResponseDTO(
                    cs.getCourse().getId(),
                    cs.getCourse().getName()
            );
            result.add(dto);
        }

        return result;
    }
}
