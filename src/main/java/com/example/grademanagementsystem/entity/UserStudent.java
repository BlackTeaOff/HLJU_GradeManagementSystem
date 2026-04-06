package com.example.grademanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "student")
public class UserStudent extends UserBase {

    @Column
    @ToString.Include
    private String major;

    @Column(name = "enrollment_year")
    @ToString.Include
    private String year;

    @Column(name = "student_group")
    @ToString.Include
    private String group;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CourseStudent> courseStudents = new HashSet<>(); // CascadeType.ALL级联操作(删除学生也会删除成绩)
}
