package com.practice.mapper;

import com.practice.model.Student;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

/**
 * Created by ravikiran_gorthi on 5/18/17.
 */
public class StudentMapperImpl implements StudentMapper {

    private int result;

    private SqlSessionTemplate sqlSession;

    public StudentMapperImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = null;
        try {
            students = sqlSession.selectList("getAllStudents");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return students;
    }

    @Override
    public Student getStudentById(int studentId) {
        Student student = (Student) sqlSession.selectOne("getStudentById", studentId);
        return student;
    }

    @Override
    public Student getStudentByName(String name) {
        Student student = (Student) sqlSession.selectOne("getStudentByName", name);
        return student;
    }

    @Override
    public int saveStudent(Student student) {
        result = sqlSession.insert("saveStudent", student);
        return result;
    }

    @Override
    public int updateStudent(Student student) {
        result = sqlSession.update("updateStudent", student);
        return result;
    }

    @Override
    public int deleteStudent(int studentId) {
        result = sqlSession.delete("deleteStudent", studentId);
        return result;
    }
}
