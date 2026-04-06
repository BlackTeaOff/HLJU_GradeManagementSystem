package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.CourseCreateRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseStudentRequestDTO;
import com.example.grademanagementsystem.dto.response.CourseInfoResponseDTO;
import com.example.grademanagementsystem.dto.response.CourseStudentResponseDTO;
import com.example.grademanagementsystem.entity.Course;
import com.example.grademanagementsystem.entity.CourseStudent;
import com.example.grademanagementsystem.entity.TeacherCourse;
import com.example.grademanagementsystem.entity.UserTeacher;
import com.example.grademanagementsystem.dto.request.CourseDeleteRequestDTO;
import com.example.grademanagementsystem.dto.request.CourseModifyRequestDTO;
import com.example.grademanagementsystem.repository.CourseRepository;
import com.example.grademanagementsystem.repository.CourseStudentRepository;
import com.example.grademanagementsystem.repository.TeacherCourseRepository;
import com.example.grademanagementsystem.repository.UserTeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional // 如果中间出现问题, 那么就全部回滚
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserTeacherRepository userTeacherRepository;

    @Autowired
    private TeacherCourseRepository teacherCourseRepository;
    @Autowired
    private CourseStudentRepository courseStudentRepository;

    @Override
    public Integer createCourse(CourseCreateRequestDTO courseCreateRequestDTO) {
        // 创建并保存课程
        Course course = new Course();
        course.setName(courseCreateRequestDTO.name());
        Course savedCourse = courseRepository.save(course);

        // 如果传了老师ID列表，给课程分配老师
        List<Integer> teacherIds = courseCreateRequestDTO.teacherIds();
        if (teacherIds != null && !teacherIds.isEmpty()) {
            for (Integer teacherId : teacherIds) {
                UserTeacher teacher = userTeacherRepository.findById(teacherId)
                        .orElseThrow(() -> new RuntimeException("老师ID不存在: " + teacherId));
                // 如果未找到老师, 那么会全部回滚, 之前创建的课程也会消失
                TeacherCourse teacherCourse = new TeacherCourse(); // teacherCourse表中加一行数据
                teacherCourse.setCourse(savedCourse); // 设置课程外键
                teacherCourse.setTeacher(teacher); // 设置教师外键
                
                teacherCourseRepository.save(teacherCourse);
            }
        }

        return savedCourse.getId();
    }

    @Override
    public void modifyCourse(CourseModifyRequestDTO courseModifyRequestDTO) {
        // 1. 查找课程
        Course course = courseRepository.findById(courseModifyRequestDTO.id())
                .orElseThrow(() -> new RuntimeException("课程ID不存在: " + courseModifyRequestDTO.id()));

        // 2. 修改课程名称（如果有传）
        if (courseModifyRequestDTO.name() != null && !courseModifyRequestDTO.name().trim().isEmpty()) { // trim会去掉空格, 过滤空字符串
            course.setName(courseModifyRequestDTO.name());
        }

        // 3. 修改老师分配（如果传了新的老师列表）
        if (courseModifyRequestDTO.teacherIds() != null) {
            // 先清空该课程原有的任课老师记录
            teacherCourseRepository.deleteByCourse(course);
            teacherCourseRepository.flush(); // 强制刷新，解决Hibernate默认先执行Insert后执行Delete导致的唯一索引冲突

            // 再重新将新的老师与课程进行绑定
            for (Integer teacherId : courseModifyRequestDTO.teacherIds()) {
                UserTeacher teacher = userTeacherRepository.findById(teacherId)
                        .orElseThrow(() -> new RuntimeException("老师ID不存在: " + teacherId));

                TeacherCourse teacherCourse = new TeacherCourse();
                teacherCourse.setCourse(course);
                teacherCourse.setTeacher(teacher);
                teacherCourseRepository.save(teacherCourse);
            }
        }
    }

    @Override
    public void deleteCourse(CourseDeleteRequestDTO courseDeleteRequestDTO) {
        // 查找课程
        Course course = courseRepository.findById(courseDeleteRequestDTO.id())
                .orElseThrow(() -> new RuntimeException("课程ID不存在: " + courseDeleteRequestDTO.id()));
        
        // 直接删除课程。由于在实体类中配了 CascadeType.ALL, 其关联的选课记录、任课记录都会自动被级联删除
        courseRepository.delete(course);
    }

    @Override
    public List<CourseInfoResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll(); // 从数据库中拿到所有课程

        List<CourseInfoResponseDTO> result = new ArrayList<>(); // 要返回的DTO数组

        for (Course cs : courses) { // 遍历courses, 把老师名字找出来然后信息组装成dto
            List<TeacherCourse> teacherCourses = teacherCourseRepository.findByCourse(cs);
            List<String> teacherNames = new ArrayList<>();
            for (TeacherCourse tc : teacherCourses) {
                teacherNames.add(tc.getTeacher().getName());
            }
            CourseInfoResponseDTO dto = new CourseInfoResponseDTO(
                    cs.getId(),
                    cs.getName(),
                    teacherNames
            );
            result.add(dto);
        }
        return result;
    }

    // 查询一个课的所有学生
    public List<CourseStudentResponseDTO> getAllStudents(CourseStudentRequestDTO courseStudentRequestDTO) {
        // 先用课程ID找到所有courseStudent关联记录
        List<CourseStudent> courseStudents = courseStudentRepository.findAllByCourse_Id(courseStudentRequestDTO.courseId());

        // 结果数组, 里面是这个课程的学生信息
        List<CourseStudentResponseDTO> result = new ArrayList<>();

        // 遍历courseStudent记录, 提取里面的Student信息, 放到DTO里之后再放到数组里
        for (CourseStudent cs : courseStudents) {
            CourseStudentResponseDTO dto = new CourseStudentResponseDTO(
                    cs.getStudent().getId(),
                    cs.getStudent().getName(),
                    cs.getStudent().getMajor(),
                    cs.getStudent().getGroup()
            );
            result.add(dto);
        }
        return result;
    }
}
