package com.green.muziuniv_be_notuser.app.professor.coursemanage.model;

import lombok.Getter;

@Getter
public class ProfessorGetRes {
    private String courseCode;
    private int courseId;
    private String deptName;
    private String classroom;
    private String type;
    private int semester;
    private String time;
    private String title;
    private int credit;
    private int maxStd;
    private int remStd;
    private String status;
    private int grade;
    private int courseStudent;
}
