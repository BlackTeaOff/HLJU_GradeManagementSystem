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
@Table(name = "student")
public class UserStudent extends UserBase {
    @Column
    private String major;
    private String year;
    private String group;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Set<CourseStudent> courseStudents = new HashSet<>();
}
