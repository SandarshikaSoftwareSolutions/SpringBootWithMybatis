package com.practice.service;

import com.practice.mapper.StudentMapperImpl;
import com.practice.model.Student;
import com.practice.testdata.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by ravikiran_gorthi on 5/24/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

    private final int result = 1;
    private List<Student> students;
    private final int id = 2;
    private final String name = "Ravi";

    @Mock
    private StudentMapperImpl studentMapper;

    @InjectMocks
    private StudentService studentService;

    @Before
    public void setUp()  throws Exception {
        students = new TestData().getStudentList();
    }

    @After
    public void tearDown() throws Exception {
        studentMapper = null;
        studentService = null;
        students = null;
    }

    @Test
    public void testGetAllStudents() throws Exception {
        when(studentMapper.getAllStudents()).thenReturn(students);

        List<Student> studentsList = studentService.getAllStudents();

        assertNotNull(students);
        assertEquals(students, studentsList);
        assertEquals(students.size(), studentsList.size());

        verify(studentMapper).getAllStudents();
        verify(studentMapper, atLeastOnce()).getAllStudents();
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new TestData().getStudent(id);

        when(studentMapper.getStudentById(anyInt())).thenReturn(student);

        Student studentReturned = (Student) studentService.getStudentById(id);

        assertNotNull(studentReturned);
        assertTrue(student.getStudentId() == studentReturned.getStudentId());

        verify(studentMapper).getStudentById(id);
        verify(studentMapper, atLeast(1)).getStudentById(id);
    }

    @Test
    public void testGetStudentByName() throws Exception {
        Student student = new TestData().getStudent(name);

        when(studentMapper.getStudentByName(anyString())).thenReturn(student);

        Student studentReturned = studentService.getStudentByName(name);

        assertNotNull(student);
        assertTrue(student.getName().equals(studentReturned.getName()));

        verify(studentMapper).getStudentByName(name);
        verify(studentMapper, atLeast(1)).getStudentByName(name);
    }

    @Test
    public void testSaveStudent() throws Exception {
        Student student = new Student(7,"Govind","VB",77,"9363223934","govind@gmail.com");

        when(studentMapper.saveStudent(anyObject())).thenReturn(result);

        int resultReturned = studentService.saveStudent(student);

        assertNotNull(resultReturned);
        assertTrue(result == resultReturned);

        verify(studentMapper).saveStudent(student);
        verify(studentMapper, atLeast(1)).saveStudent(student);
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student(7,"Govind","VB",77,"9363223934","govind@gmail.com");

        when(studentMapper.updateStudent(anyObject())).thenReturn(result);

        int resultReturned = studentService.updateStudent(student);

        assertNotNull(resultReturned);
        assertTrue(result == resultReturned);

        verify(studentMapper).updateStudent(student);
        verify(studentMapper, atLeast(1)).updateStudent(student);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        int id = 7;

        when(studentMapper.deleteStudent(anyInt())).thenReturn(result);

        int resultReturned = studentService.deleteStudent(id);

        assertNotNull(resultReturned);
        assertTrue(result == resultReturned);

        verify(studentMapper).deleteStudent(id);
        verify(studentMapper, atLeast(1)).deleteStudent(id);

        verify(studentMapper).deleteStudent(id);
    }
}