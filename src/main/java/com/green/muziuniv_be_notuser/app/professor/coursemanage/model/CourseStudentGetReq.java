package com.green.muziuniv_be_notuser.app.professor.coursemanage.model;


import lombok.Getter;

@Getter
public class CourseStudentGetReq {
    private int loginId;
    private String userName;
    private String deptName;
    private String email;
    private String phone;
    private int grade;
    private int enrollmentId;
}