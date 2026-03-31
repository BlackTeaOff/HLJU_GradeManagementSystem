package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.UserCreateDTO;
import com.example.grademanagementsystem.dto.requeset.StudentCreatedRequestDTO;
import com.example.grademanagementsystem.entity.UserAdmin;
import com.example.grademanagementsystem.entity.UserBase;
import com.example.grademanagementsystem.entity.UserStudent;
import com.example.grademanagementsystem.entity.UserTeacher;
import com.example.grademanagementsystem.repository.UserBaseRepository;
import com.example.grademanagementsystem.repository.UserStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private UserStudentRepository userStudentRepository;

    @Override
    public void createUser(UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getRole().equals("student")) {
            UserStudent student = new UserStudent();
            student.setName(userCreateDTO.getName());
            student.setPassword(userCreateDTO.getPassword());
            student.setYear(userCreateDTO.getEnrollment_year());
            student.setMajor(userCreateDTO.getMajor());
            student.setGroup(userCreateDTO.getStudent_group());
            userStudentRepository.save(student);
        } else {
            UserBase user = new UserBase();
            user.setName(userCreateDTO.getName());
            user.setPassword(userCreateDTO.getPassword());
            userBaseRepository.save(user);
        }
    }

    public void createStudent(StudentCreatedRequestDTO studentCreatedRequestDTO) {
        UserStudent student = new UserStudent();
        student.setName(studentCreatedRequestDTO.name());
        student.setPassword(studentCreatedRequestDTO.password());
        student.setYear(studentCreatedRequestDTO.enrollment_year());
        student.setMajor(studentCreatedRequestDTO.major());
        student.setGroup(studentCreatedRequestDTO.student_group());
        userStudentRepository.save(student);
    }

    public void createTeacher(StudentCreatedRequestDTO studentCreatedRequestDTO) {
        UserStudent teacher = new UserStudent();
        teacher.setName(userCreateDTO.getName());
        teacher.setPassword(userCreateDTO.getPassword());
        teacher.setYear(userCreateDTO.getEnrollment_year());
        teacher.setMajor(userCreateDTO.getMajor());
        teacher.setGroup(userCreateDTO.getStudent_group());
        userStudentRepository.save(teacher);
    }

    public void createAdmin(StudentCreatedRequestDTO studentCreatedRequestDTO) {
        UserAdmin = new UserAdmin();
        UserAdmin ;
        student.setName(userCreateDTO.getName());
        student.setPassword(userCreateDTO.getPassword());
        student.setYear(userCreateDTO.getEnrollment_year());
        student.setMajor(userCreateDTO.getMajor());
        student.setGroup(userCreateDTO.getStudent_group());
        userStudentRepository.save(student);
    }
}
