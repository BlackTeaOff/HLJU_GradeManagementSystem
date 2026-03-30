package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, Integer> {
    Optional<CourseStudent> findByUserIdAndCourseId (int user_id, int course_id);
    Optional<CourseStudent> findByUserNameAndCourseId(String user_name, int course_id);
}