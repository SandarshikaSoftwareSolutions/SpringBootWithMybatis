package com.practice.controller;

import com.practice.model.Student;
import com.practice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ravikiran_gorthi on 5/18/17.
 */
@Controller
public class StudentController {

    private StudentService studentService;
    private final MessageSource messageSource;

    @Autowired
    public StudentController(MessageSource messageSource, StudentService studentService) {
        this.messageSource = messageSource;
        this.studentService = studentService;
    }

    @Value("${welcome.message}")
    private String message;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView welcome(ModelAndView model) {
        model.addObject("message", this.message);
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String showStudentsPage(ModelMap model) {
        model = getDefaultStudentModel(model);
        model.addAttribute("student", new Student());
        model.addAttribute("studentsList", studentService.getAllStudents());
        model.addAttribute("mybatisMessage", null);

        return "students";
    }

    @RequestMapping(value = "/getStudent/{studentId}", method = RequestMethod.GET)
    public String getStudentById(@PathVariable("studentId") int studentId, ModelMap model) {
        Student student = studentService.getStudentById(studentId);
        model = getDefaultStudentModel(model);
        if (student != null) {
            model.addAttribute("student", student);
            model.addAttribute("mybatisMessage", null);
        } else {
            model.addAttribute("mybatisMessage", "Student was not found in Database with Id = " + studentId);
        }
        return "students";
    }

    @RequestMapping(value = "/saveStudent", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute("student") Student student, ModelMap model, BindingResult result) {

        Student existingStudent = studentService.getStudentByName(student.getName());
        model = getDefaultStudentModel(model);
        if (existingStudent != null) {
            model.addAttribute("mybatisMessage", "Student already exists in the Database!");
            model.addAttribute("student", student);
            return "redirect:/students";
        } else {
            if (studentService.saveStudent(student) < 1) {
                model.addAttribute("mybatisMessage", "Invalid Student Details !");
                model.addAttribute("errors", result);
                return "students";
            } else {
                model.addAttribute("mybatisMessage", "Student saved Successfully !");
                return "redirect:/students";
            }
        }

    }

    @RequestMapping(value = "/updStudent", method = RequestMethod.POST)
    public String updateStudent(@ModelAttribute("student") Student student, ModelMap model, BindingResult result) {

        if (studentService.updateStudent(student) < 1) {
            model.addAttribute("mybatisMessage", "Unable to update the Student with Id = " + student.getStudentId() + " !");
            model.addAttribute("errors", result);
            model.addAttribute("student", student);
            return "students";
        } else {
            model.addAttribute("mybatisMessage", "Student updated Successfully !");
            return "redirect:/students";
        }
    }

    @RequestMapping(value = "/deleteStudent/{studentId}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable("studentId") int studentId, ModelMap model) {
        if (studentService.deleteStudent(studentId) < 1) {
            model.addAttribute("mybatisMessage", "Unable to delete the Student with Id = " + studentId + " !");
            return "students";
        } else {
            model.addAttribute("mybatisMessage", "Deleted the Student with Id = " + studentId + " !");
            return "redirect:/students";
        }
    }

    private ModelMap getDefaultStudentModel(ModelMap model) {
        model.addAttribute("studentsList", studentService.getAllStudents());

        return model;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(HttpServletRequest request, Exception e, ModelAndView model) {
        model = new ModelAndView("error");
        model.addObject("exception", e);
        return model;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpServletRequest request, Exception e, ModelAndView model) {
        model = new ModelAndView("error");
        model.addObject("exception", e);
        return model;
    }

}
