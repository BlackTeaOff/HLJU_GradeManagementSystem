package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.UserCreateDTO;
import com.example.grademanagementsystem.dto.requeset.*;
import com.example.grademanagementsystem.entity.UserAdmin;
import com.example.grademanagementsystem.entity.UserBase;
import com.example.grademanagementsystem.entity.UserStudent;
import com.example.grademanagementsystem.entity.UserTeacher;
import com.example.grademanagementsystem.repository.UserAdminRepository;
import com.example.grademanagementsystem.repository.UserBaseRepository;
import com.example.grademanagementsystem.repository.UserStudentRepository;
import com.example.grademanagementsystem.repository.UserTeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private UserStudentRepository userStudentRepository;

    @Autowired
    private UserTeacherRepository userTeacherRepository;

    @Autowired
    private UserAdminRepository userAdminRepository;

    /***@Override
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
    }***/

    @Override
    public void createStudent(StudentCreatedRequestDTO studentCreatedRequestDTO) {
        UserStudent student = new UserStudent();
        student.setName(studentCreatedRequestDTO.name());
        student.setPassword(studentCreatedRequestDTO.password());
        student.setYear(studentCreatedRequestDTO.enrollment_year());
        student.setMajor(studentCreatedRequestDTO.major());
        student.setGroup(studentCreatedRequestDTO.student_group());
        userStudentRepository.save(student);
    }

    @Override
    public void createTeacher(TeacherCreatedRequestDTO teacherCreatedRequestDTO) {
        UserTeacher teacher = new UserTeacher();
        teacher.setName(teacherCreatedRequestDTO.name());
        teacher.setPassword(teacherCreatedRequestDTO.password());
        teacher.setDepartment(teacherCreatedRequestDTO.department());
        userTeacherRepository.save(teacher);
    }

    @Override
    public void createAdmin(AdminCreatedRequestDTO adminCreatedRequestDTO) {
        UserAdmin userAdmin = new UserAdmin();
        userAdmin.setName(adminCreatedRequestDTO.name());
        userAdmin.setPassword(adminCreatedRequestDTO.password());
        userAdmin.setLevel(adminCreatedRequestDTO.level());
        userAdminRepository.save(userAdmin);
    }

    @Override
    public void login(UserLoginDTO userLoginDTO) {
        Optional<UserBase> op = userBaseRepository.findById(userLoginDTO.id());
        if (op.isPresent()) {
            if ( op.get().getPassword().equals(userLoginDTO.password()) ) {
                //生成token？
                //提示：恭喜你，登录成功！
            }
        } else {//抛出异常：id不存在
        }
    }

    @Override
    public void deleteUser(UserDeletedRequestDTO userDeletedRequestDTO) {
        Optional<UserBase> op = userBaseRepository.findById(userDeletedRequestDTO.id());
        if (op.isPresent()) {
            if ( op.get().getPassword().equals(userDeletedRequestDTO.password()) ) {
                //生成token？
                userBaseRepository.deleteById(op.get().getId());
                //提示：恭喜你，注销成功！
            }
        } else {//抛出异常：id不存在
        }
    }

    @Override
    public void modifiedPassword(PasswordModifiedRequestDTO passwordModifiedRequestDTO) {
        Optional<UserBase> op = userBaseRepository.findById(passwordModifiedRequestDTO.id());
        if (op.isPresent()) {
            if ( op.get().getPassword().equals(passwordModifiedRequestDTO.old_password()) ) {
                //生成token？
                op.get().setPassword(passwordModifiedRequestDTO.new_password());
                //提示：恭喜你，修改成功！
            }
        } else {//抛出异常：id不存在
        }
    }
}
