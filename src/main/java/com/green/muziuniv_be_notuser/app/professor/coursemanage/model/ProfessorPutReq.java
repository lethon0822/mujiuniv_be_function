package com.green.muziuniv_be_notuser.app.professor.coursemanage.model;

import lombok.Getter;

@Getter

public class ProfessorPutReq {
    private Long courseId;
    private String classroom;
    private String type;
    private Long semesterId;
    private String time;
    private String title;
    private Long credit;
    private String weekPlan;
    private String textBook;
    private String goal;
    private Integer maxStd;
    private int grade;
}
