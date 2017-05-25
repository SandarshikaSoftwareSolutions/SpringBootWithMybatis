package com.practice.mapper;

import com.practice.model.Student;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

/**
 * Created by ravikiran_gorthi on 5/18/17.
 */
public interface StudentMapper {

    List<Student> getAllStudents();

    Student getStudentById(int id);

    Student getStudentByName(String name);

    int saveStudent(Student student);

    int updateStudent(Student student);

    int deleteStudent(int id);

    void setSqlSession(SqlSessionTemplate sqlSession);

}
