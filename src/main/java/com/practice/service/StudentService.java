package com.practice.service;

import com.practice.mapper.StudentMapper;
import com.practice.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ravikiran_gorthi on 5/15/17.
 */
@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public List<Student> getAllStudents() {
        return studentMapper.getAllStudents();
    }

    public Student getStudentById(int id) {
        return studentMapper.getStudentById(id);
    }

    public Student getStudentByName(String name) {
        return studentMapper.getStudentByName(name);
    }

    @Transactional
    public int saveStudent(Student student) {
        return studentMapper.saveStudent(student);
    }

    @Transactional
    public int updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Transactional
    public int deleteStudent(int id) {
        return studentMapper.deleteStudent(id);
    }

}