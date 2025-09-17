package com.green.muziuniv_be_notuser.app.shared.course.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseDetailRes {
    private Long courseId;
    private String classroom;
    private String type;
    private Integer semester ;
    private String time;
    private String title;
    private Long credit;
    private String weekPlan;
    private String textBook;
    private String goal;
    private int maxStd;
    private String userName;
    private String deptName;
    private Integer grade;
}
