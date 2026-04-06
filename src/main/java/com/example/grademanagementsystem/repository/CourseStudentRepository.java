package com.example.grademanagementsystem.repository;

import com.example.grademanagementsystem.entity.Course;
import com.example.grademanagementsystem.entity.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, Integer> {
    // 根据学生id和课程id找课程和学生的关联, 这里面Student代表去CourseStudent里找student, 然后去student里找id
    Optional<CourseStudent> findByStudent_IdAndCourse_Id(Integer studentId, Integer courseId);

    // 根据学生ID查询所有关联的课程成绩
    List<CourseStudent> findAllByStudent_Id(Integer studentId);

    // 根据老师ID查询他教的课程下所有学生的成绩
    // 把courseStudent表和teacherCourse表根据course_idJOIN一下. 两个表都有course_id, 所以可以通过这个由老师找到courseStudent记录
    //@Query(value = "SELECT cs.* FROM course_student cs JOIN teacher_course tc ON cs.course_id = tc.course_id WHERE tc.teacher_id = :teacherId", nativeQuery = true)
    //List<CourseStudent> findAllByTeacherId(@Param("teacherId") Integer teacherId); // @Param给上面@Query传参

    // 根据课程ID查所有与与这个课程关联的CourseStudent
    List<CourseStudent> findAllByCourse_Id(Integer courseId);

    // 根据课程ID和学生ID找课程和学生的关联记录, 用来防止学生重复选课
    boolean existsByCourse_IdAndStudent_Id(int courseId, int student_Id);
}