package com.practice.model;

import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 2264535351943252507L;

    private int studentId;

    //@NotNull(message = "Student name cannot be Empty")
    private String name;

    //@NotNull(message = "Student branch cannot be Empty")
    private String branch;

    //@Pattern(regexp="\\d{3}", message = "Student percentage should contain 3 digits only")
    //@NotNull(message = "Student percentage cannot be Null")
    private int percentage;

    //@Pattern(regexp="\\d+", message = "Student Phone number should contain 10 digits only")
    //@Size(min = 10, max = 10)
    private String mobileNo;

    //@Pattern(regexp = "^([a-zA-Z0-9\\-\\.\\_]+)'+'(\\@)([a-zA-Z0-9\\-\\.]+)'+'(\\.)([a-zA-Z]{2,4})$",
    //       message = "Invalid email address")
    private String emailId;

    //Constructor with No-args
    public Student() {
    }

    //Constructor with all Parameters
    public Student(int studentId, String name, String branch, int percentage, String mobileNo, String emailId) {
        this.studentId = studentId;
        this.name = name;
        this.branch = branch;
        this.percentage = percentage;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
    }

    //Constructor with all Parameters without Student Id
    public Student(String name, String branch, int percentage, String mobileNo, String emailId) {
        this.name = name;
        this.branch = branch;
        this.percentage = percentage;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
    }

    //Setters and getters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", branch='" + branch + '\'' +
                ", percentage=" + percentage +
                ", mobileNo=" + mobileNo +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}