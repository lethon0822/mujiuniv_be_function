package com.green.muziuniv_be_notuser.app.professor.coursemanage.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfessorPostReq {
    private Long userId;
    private String courseCode;
    private String classroom;
    private int semesterId;
    private String type;
    private String time;
    private String title;
    private int credit;
    private String weekPlan;
    private String textBook;
    private String goal;
    private int maxStd;
    private int grade;
}
