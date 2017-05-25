package com.practice.testdata;

import com.practice.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravikiran_gorthi on 5/25/17.
 */
public class TestData {
    private List<Student> studentList;
    private Student student;

    public List<Student> getStudentList() {
        studentList = new ArrayList<>();
        studentList.add(new Student(1,"Bhavya","HR",67,"7865377434","bhavya@gmail.com"));
        studentList.add(new Student(2,"Ravi","Java",76,"8987979898","ravi@gmail.com"));
        studentList.add(new Student(3,"Chandra","Test",84,"9898989898","chandra@gmail.com"));
        studentList.add(new Student(4,"Neeta","Transactions",88,"9697986987","neeta@gmail.com"));
        studentList.add(new Student(5,"Shafi","HR",75,"9493837643","shafi@gmail.com"));
        studentList.add(new Student(6,"Niyaz","VB",87,"9368468934","niyaz@gmail.com"));

        return studentList;
    }

    public Student getStudent(int id) {
        for(Student studentReturned : getStudentList()) {
            if(studentReturned.getStudentId() == id) {
                student = studentReturned;
                break;
            }
        }
        return student;
    }

    public Student getStudent(String name) {
        for(Student studentReturned : getStudentList()) {
            if(studentReturned.getName().equals(name)) {
                student = studentReturned;
                break;
            }
        }
        return student;
    }
}
