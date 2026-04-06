package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.GradeInputRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherCourseRequestDTO;
import com.example.grademanagementsystem.dto.request.TeacherGradeQueryRequestDTO;
import com.example.grademanagementsystem.dto.response.TeacherCourseResponseDTO;
import com.example.grademanagementsystem.dto.response.TeacherGradeResponseDTO;
import com.example.grademanagementsystem.entity.CourseStudent;
import com.example.grademanagementsystem.entity.TeacherCourse;
import com.example.grademanagementsystem.repository.CourseStudentRepository;
import com.example.grademanagementsystem.repository.TeacherCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    CourseStudentRepository courseStudentRepository; // 课程学生关联表(和成绩有关)

    @Autowired
    TeacherCourseRepository teacherCourseRepository; //  老师课程关联表
    /*
    @Override
    public List<TeacherGradeResponseDTO> queryGrades(GradeQueryRequestDTO gradeQueryRequestDTO) {
        // 通过teacher_id找course_id, 之后由course_id找courseStudent记录, 里面有成绩
        List<CourseStudent> courseStudents = courseStudentRepository.findAllByTeacherId(gradeQueryRequestDTO.id());
        
        // 创建一个空的返回列表(返回给Controller), 里面有学生和成绩(因为老师查看成绩)
        List<TeacherGradeResponseDTO> result = new java.util.ArrayList<>();
        
        // 遍历查询出的所有关联记录
        for (CourseStudent cs : courseStudents) {
            // 过滤掉还没录入成绩的
            if (cs.getGrade() != null) {
                // 转换并封装成 DTO
                TeacherGradeResponseDTO dto = new TeacherGradeResponseDTO(
                        cs.getStudent().getId(),
                        cs.getStudent().getName(), // Requires UserStudent to inherit/have getName()
                        cs.getCourse().getId(),
                        cs.getCourse().getName(), 
                        cs.getGrade()
                );
                result.add(dto); // 把这个dto加入到返回列表里
            }
        }
        
        return result;
    }
     */

    @Override
    public List<TeacherGradeResponseDTO> queryGrades(TeacherGradeQueryRequestDTO teacherGradeQueryRequestDTO) {
        // 检查老师是否和这门课关联
        boolean isTeaching = teacherCourseRepository.existsByTeacher_IdAndCourse_Id(teacherGradeQueryRequestDTO.teacherId(), teacherGradeQueryRequestDTO.courseId());
        if (!isTeaching) {
            throw new RuntimeException("您未教授该课程！"); // 没找到老师和课程的关联信息就抛异常
        }
        // 通过课程id查到学生和课程的关联记录
        List<CourseStudent> courseStudents = courseStudentRepository.findAllByCourse_Id(teacherGradeQueryRequestDTO.courseId());

        // 查到的课程信息存到数组里
        List<TeacherGradeResponseDTO> result = new ArrayList<>();

        // 把查出来的成绩信息打包成DTO返回
        for (CourseStudent cs : courseStudents) {
            TeacherGradeResponseDTO dto = new TeacherGradeResponseDTO(
                    cs.getStudent().getId(),
                    cs.getStudent().getName(),
                    cs.getCourse().getId(),
                    cs.getCourse().getName(),
                    cs.getGrade()
            );
            result.add(dto);
        }
        return result;
    }

    @Override
    public void inputGrade(int teacherId, GradeInputRequestDTO gradeInputRequestDTO) {
        // 先检查该老师是否教这门课
        boolean isTeaching = teacherCourseRepository.existsByTeacher_IdAndCourse_Id(teacherId, gradeInputRequestDTO.courseId());
        if (!isTeaching) {
            throw new RuntimeException("您未教授该课程，无权录入成绩！");
        }

        CourseStudent courseStudent = courseStudentRepository.findByStudent_IdAndCourse_Id(gradeInputRequestDTO.studentId(), gradeInputRequestDTO.courseId())
                .orElseThrow(() -> new RuntimeException("学生未选该课程")); // 只有找不到时才new Exception, 没有课程和学生的关联就代表没选这个课
        
        if (courseStudent.getGrade() != null) {
            throw new RuntimeException("成绩已录入，请使用修改成绩功能");
        }
        
        courseStudent.setGrade(gradeInputRequestDTO.grade());
        courseStudentRepository.save(courseStudent);
    }

    @Override
    public void modifyGrade(int teacherId, GradeInputRequestDTO gradeInputRequestDTO) {
        // 先检查是否有权限修改
        boolean isTeaching = teacherCourseRepository.existsByTeacher_IdAndCourse_Id(teacherId, gradeInputRequestDTO.courseId());
        if (!isTeaching) {
            throw new RuntimeException("您未教授该课程，无权修改成绩！");
        }

        CourseStudent courseStudent = courseStudentRepository.findByStudent_IdAndCourse_Id(gradeInputRequestDTO.studentId(), gradeInputRequestDTO.courseId())
                .orElseThrow(() -> new RuntimeException("学生未选该课程"));

        if (courseStudent.getGrade() == null) {
            throw new RuntimeException("成绩尚未录入，请先录入成绩");
        }

        courseStudent.setGrade(gradeInputRequestDTO.grade());
        courseStudentRepository.save(courseStudent); 
    }

    @Override
    // 用来老师查询自己教的课程
    public List<TeacherCourseResponseDTO> queryCourses(TeacherCourseRequestDTO teacherCourseRequestDTO) {
        // 通过teacher来找到teacherCourse记录, 以此找到Course
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findByTeacher_Id(teacherCourseRequestDTO.teacherId());

        List<TeacherCourseResponseDTO> result = new ArrayList<>(); // 要返回的数组

        // 把课程信息打包成dto放到数组里
        for (TeacherCourse tc : teacherCourses) {
            TeacherCourseResponseDTO dto = new TeacherCourseResponseDTO(
                    tc.getCourse().getId(),
                    tc.getCourse().getName()
            );
            result.add(dto);
        }
        return result;
    }
}
