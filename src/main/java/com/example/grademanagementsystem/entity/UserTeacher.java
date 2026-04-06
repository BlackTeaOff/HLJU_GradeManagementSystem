package com.example.grademanagementsystem.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true) // 因为id在父类, 所以要能调用父类
public class UserTeacher extends UserBase {

    @Column
    private String department;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherCourse> teacherCourses = new HashSet<>();
}
