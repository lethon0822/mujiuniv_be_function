package com.green.muziuniv_be_notuser.app.student.grade.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllPermanentGradeRes {
    private int year;
    private int semester;
    private String type;
    private int grade;
    private Long userId; //담당 교수 번호
    private String professorName;
    private String courseCode;
    private String title;
    private int credit;
    private String rank;
    private double point; // 평점 4.5, 4.0 ...
}
