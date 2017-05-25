package com.practice.mapper;

import com.practice.model.Student;
import com.practice.testdata.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ravikiran_gorthi on 5/24/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class StudentMapperImplTest {

    private final int result = 1;
    private List<Student> students;

    @Mock
    private SqlSessionTemplate sqlSession;

    @InjectMocks
    private StudentMapperImpl studentMapper;


    @Before
    public void setUp()  throws Exception {
        students = new TestData().getStudentList();
    }

    @After
    public void tearDown() throws Exception {
        sqlSession = null;
        studentMapper = null;
    }

    @Test
    public void testGetAllStudents() throws Exception {
        when(sqlSession.selectList(any(String.class))).thenReturn(asList(students));

        List<Student> studentList = studentMapper.getAllStudents(); //Actual

        assertEquals(students, studentList.get(0));
        assertEquals(students.size(), ((List<Student>)studentList.get(0)).size());

        verify(sqlSession).selectList("getAllStudents");
    }

    @Test
    public void testGetStudentById() throws Exception {
        int id = 2;
        Student student = new TestData().getStudent(id);

        when(sqlSession.selectOne(any(String.class), any(Integer.class))).thenReturn(student);

        Student studentReturned = studentMapper.getStudentById(id);
        assertNotNull(studentReturned);
        assertTrue(student.getStudentId() == studentReturned.getStudentId());

        verify(sqlSession).selectOne("getStudentById", id);
    }

    @Test
    public void testGetStudentByName() throws Exception {
        String name = "Ravi";
        Student student = new TestData().getStudent(name);

        when(sqlSession.selectOne(any(String.class), any(String.class))).thenReturn(student);

        Student studentReturned = studentMapper.getStudentByName(name);

        assertNotNull(student);
        assertTrue(student.getName().equals(studentReturned.getName()));

        verify(sqlSession).selectOne("getStudentByName", name);
    }

    @Test
    public void testSaveStudent() throws Exception {
        Student student = new Student(7,"Govind","VB",77,"9363223934","govind@gmail.com");

        when(sqlSession.insert(any(String.class), any(Student.class))).thenReturn(result);

        int resultReturned = studentMapper.saveStudent(student);

        assertTrue(result == resultReturned);

        verify(sqlSession).insert("saveStudent", student);
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student(7,"Govind","VB",77,"9363223934","govind@gmail.com");

        when(sqlSession.update(any(String.class), any(Student.class))).thenReturn(result);

        int resultReturned = studentMapper.updateStudent(student);

        assertTrue(result == resultReturned);

        verify(sqlSession).update("updateStudent", student);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        int id = 7;

        when(sqlSession.delete(any(String.class), any(Object.class))).thenReturn(result);

        int resultReturned = studentMapper.deleteStudent(id);

        assertTrue(result == resultReturned);

        verify(sqlSession).delete("deleteStudent", id);
    }
}