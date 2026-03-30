package com.example.grademanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "stu_cour_rel", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
public class CourseStudent {//可序列接口？
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserStudent student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column
    private int grade;

    //审计字段？
}
