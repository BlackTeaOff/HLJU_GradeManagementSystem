package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.UserStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStudentRepository extends JpaRepository<UserStudent, Integer> {
    //查找
    //增 删 改
}
