package com.example.grademanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private int id;

    @Column(nullable = false, unique = true)
    @ToString.Include
    private String name;

    // 下面这两个只是为了级联操作, 否则可以直接删掉(删除课程时, 自动删除所有课程和其他的关联)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CourseStudent> courseStudents = new HashSet<>();
    // 因为这个关联关系可以直接在对应的Repository里查到
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherCourse> teacherCourses = new HashSet<>();
}
