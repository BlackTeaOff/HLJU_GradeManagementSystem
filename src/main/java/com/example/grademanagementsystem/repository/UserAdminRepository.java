package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdminRepository extends JpaRepository<UserAdmin, Integer> {
}
