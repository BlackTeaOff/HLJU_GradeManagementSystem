package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.TeacherCourse;
import com.example.grademanagementsystem.entity.Course;
import com.example.grademanagementsystem.entity.UserTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Integer> {
    void deleteByCourse(Course course); // 根据课程删除老师和课程的关联关系

    boolean existsByTeacher_IdAndCourse_Id(int teacher_id, int course_id);

    List<TeacherCourse> findByCourse(Course course); // 通过课程找老师

    List<TeacherCourse> findByTeacher_Id(int id);
} // 老师和课程中间表的数据库接口
