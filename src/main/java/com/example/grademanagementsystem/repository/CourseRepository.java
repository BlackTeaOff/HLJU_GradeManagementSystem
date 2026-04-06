package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}

