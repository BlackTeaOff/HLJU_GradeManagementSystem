package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.UserBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBaseRepository extends JpaRepository<UserBase, Integer> {
    Optional<UserBase> findById(int id);
    Optional<UserBase> findByName(String name);
}

//查看成绩  学生  findbyid/name  写在一还是写在多？
//

//修改密码  用户  怎么改 先查询

//修改成   老师  findebyid/name

//录入成绩  老师

//用户账户管理 增删用户    管管

//课程信息管理 增删课程    管理