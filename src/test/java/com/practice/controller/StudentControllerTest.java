package com.practice.controller;

import com.practice.model.Student;
import com.practice.service.StudentService;
import com.practice.testdata.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by ravikiran_gorthi on 5/25/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {
    private final int result = 1;
    private final int id = 2;
    private final int delId = 7;
    private final String name = "Ravi";
    private final String studentViewId = "students";
    private final String redirectViewId = "redirect:/students";
    private final String initialViewId = "index";
    private final ModelMap modelMap = new ModelMap();
    private final ModelAndView model = new ModelAndView();
    private List<Student> students;

    @Mock
    private StudentService studentService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private StudentController studentController;

    @Before
    public void setUp()  throws Exception {
        students = new TestData().getStudentList();
    }

    @After
    public void tearDown() throws Exception {
        studentService = null;
        students = null;
    }
    @Test
    public void testWelcome() throws Exception {
        final ModelAndView model = studentController.welcome(new ModelAndView());
        assertEquals("View Ids are matching", model.getViewName(), initialViewId);
    }

    @Test
    public void testShowStudentsPage() throws Exception {
        when(studentService.getAllStudents()).thenReturn(students);

        final String viewId = studentController.showStudentsPage(modelMap);
        List<Student> studentList = studentService.getAllStudents();

        assertNotNull(students);
        assertEquals(students.size(), studentList.size());
        assertEquals("View Ids are matching", viewId, studentViewId);

        verify(studentService, atLeastOnce()).getAllStudents();
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new TestData().getStudent(id);

        when(studentService.getStudentById(any(Integer.class))).thenReturn(student);
        Student studentReturned = studentService.getStudentById(id);

        String viewId = studentController.getStudentById(id, modelMap);

        assertNotNull(student);
        assertEquals(student.getStudentId(), studentReturned.getStudentId());
        assertEquals("View Ids are matching", viewId, studentViewId);

        verify(studentService, atLeastOnce()).getAllStudents();
    }

    @Test
    public void testSaveStudent() throws Exception {
        Student student = new Student(7,"Govind","VB",77,"9363223934","govind@gmail.com");

        when(studentService.saveStudent(any(Student.class))).thenReturn(result);

        int resultReturned = studentService.saveStudent(student);
        String viewId = studentController.saveStudent(student, modelMap, bindingResult);

        assertEquals(result, resultReturned);
        assertEquals("View Ids are matching", viewId, redirectViewId);

        verify(studentService, atLeastOnce()).saveStudent(student);
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student(7,"Govind","VB",77,"9363223934","govind@gmail.com");

        when(studentService.updateStudent(any(Student.class))).thenReturn(result);

        int resultReturned = studentService.updateStudent(student);
        String viewId = studentController.updateStudent(student, modelMap, bindingResult);

        assertEquals(result, resultReturned);
        assertEquals("View Ids are matching", viewId, redirectViewId);

        verify(studentService, atLeastOnce()).updateStudent(student);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        when(studentService.deleteStudent(any(Integer.class))).thenReturn(result);

        int resultReturned = studentService.deleteStudent(delId);
        String viewId = studentController.deleteStudent(delId, modelMap);

        assertEquals(result, resultReturned);
        assertEquals("View Ids are matching", viewId, redirectViewId);

        verify(studentService, atLeastOnce()).deleteStudent(delId);
    }
}