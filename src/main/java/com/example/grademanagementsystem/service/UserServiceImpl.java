package com.example.grademanagementsystem.service;

import com.example.grademanagementsystem.dto.request.*;
import com.example.grademanagementsystem.dto.response.AdminInfoResponseDTO;
import com.example.grademanagementsystem.dto.response.StudentInfoResponseDTO;
import com.example.grademanagementsystem.dto.response.TeacherInfoResponseDTO;
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

import java.util.ArrayList;
import java.util.List;
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

    /*@Override
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
    }*/

    @Override
    public Integer createStudent(StudentCreateRequestDTO studentCreateRequestDTO) {
        UserStudent student = new UserStudent();
        student.setName(studentCreateRequestDTO.name());
        student.setPassword(studentCreateRequestDTO.password());
        student.setYear(studentCreateRequestDTO.enrollment_year());
        student.setMajor(studentCreateRequestDTO.major());
        student.setGroup(studentCreateRequestDTO.student_group());
        UserStudent savedStudent = userStudentRepository.save(student);
        return savedStudent.getId();
    }

    @Override
    public Integer createTeacher(TeacherCreateRequestDTO teacherCreateRequestDTO) {
        UserTeacher teacher = new UserTeacher();
        teacher.setName(teacherCreateRequestDTO.name());
        teacher.setPassword(teacherCreateRequestDTO.password());
        teacher.setDepartment(teacherCreateRequestDTO.department());
        UserTeacher savedTeacher = userTeacherRepository.save(teacher);
        return savedTeacher.getId();
    }

    @Override
    public Integer createAdmin(AdminCreateRequestDTO adminCreateRequestDTO) {
        UserAdmin userAdmin = new UserAdmin();
        userAdmin.setName(adminCreateRequestDTO.name());
        userAdmin.setPassword(adminCreateRequestDTO.password());
        userAdmin.setLevel(adminCreateRequestDTO.level());
        UserAdmin savedAdmin = userAdminRepository.save(userAdmin);
        return savedAdmin.getId();
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) { // 传入登录DTO(含ID和密码)
        Optional<UserBase> op = userBaseRepository.findById(userLoginDTO.id());
        if (op.isPresent()) { // 因为数据库里可能没有该id的对象, 所以用optional, 可为空
            if (op.get().getPassword().equals(userLoginDTO.password())) {
                // 1. 登录成功，生成并返回 Token（这里暂时用简单字符串模拟，后续可引入JWT库）
                return "token-user-" + op.get().getId();
            } else {
                // 2. 密码错误
                throw new RuntimeException("密码错误"); // 抛出的异常都交给ExceptionHandler处理
            }
        } else {
            // 3. 用户不存在
            throw new RuntimeException("该用户ID不存在");
        }
    }

    @Override
    public void deleteUser(UserDeleteRequestDTO userDeleteRequestDTO) {
        Optional<UserBase> op = userBaseRepository.findById(userDeleteRequestDTO.id());
        if (op.isPresent()) {
            if (op.get().getPassword().equals(userDeleteRequestDTO.password())) {
                userBaseRepository.deleteById(op.get().getId()); // delete方法不需要写save
                // 提示：恭喜你，注销成功！
            } else {
                throw new RuntimeException("密码错误");
            }
        } else { // 抛出异常：id不存在
            throw new RuntimeException("该用户ID不存在");
        }
    }

    @Override
    public void modifyPassword(PasswordModifyRequestDTO passwordModifyRequestDTO) {
        Optional<UserBase> op = userBaseRepository.findById(passwordModifyRequestDTO.id());
        if (op.isPresent()) {
            if (op.get().getPassword().equals(passwordModifyRequestDTO.old_password())) {
                op.get().setPassword(passwordModifyRequestDTO.new_password());
                userBaseRepository.save(op.get());
            } else {
                throw new RuntimeException("原密码错误");
            }
        } else { // 抛出异常：id不存在
            throw new RuntimeException("该用户ID不存在");
        }
    }


    @Override
    // 可能返回Student/Teacher/AdminResponseDTO, 所以返回Object(是所有类的父类), 否则就需要创建一个这三个的父类(接口)
    public Object queryInfo(InfoQueryRequestDTO dto) {
        // 不知道用户是什么类型, 先去user表里查
        UserBase user = userBaseRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("未找到该用户"));

        if (user instanceof UserStudent student) {
            return new StudentInfoResponseDTO(student.getId(), student.getName(), student.getMajor(), student.getYear(), student.getGroup());
        } else if (user instanceof UserTeacher teacher) {
            return new TeacherInfoResponseDTO(teacher.getId(), teacher.getName(), teacher.getDepartment());
        } else if (user instanceof UserAdmin admin) {
            return new AdminInfoResponseDTO(admin.getId(), admin.getName(), admin.getLevel());
        } else {
            throw new RuntimeException("未知的用户类型");
        }
    }

    @Override
    // 管理员查看所有用户列表
    public List<Object> queryAllUsers() {
        // 先在UserBase表中找到所有用户
        List<UserBase> allUsers = userBaseRepository.findAll();

        // 因为有多种返回类型, 所以用他们的父类Object
        List<Object> result = new ArrayList<>();

        for (UserBase user : allUsers) { // 根据不同用户类型向result数组添加不同的DTO
            if (user instanceof UserStudent student) {
                result.add(new StudentInfoResponseDTO(student.getId(), student.getName(), student.getMajor(), student.getYear(), student.getGroup()));
            } else if (user instanceof UserTeacher teacher) {
                result.add(new TeacherInfoResponseDTO(teacher.getId(), teacher.getName(), teacher.getDepartment()));
            } else if (user instanceof UserAdmin admin) {
                result.add(new AdminInfoResponseDTO(admin.getId(), admin.getName(), admin.getLevel()));
            } else {
                throw new RuntimeException("未知的用户类型");
            }
        }

        return result;
    }
}
