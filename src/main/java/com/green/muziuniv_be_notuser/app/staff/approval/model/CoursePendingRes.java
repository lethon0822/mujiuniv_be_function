// 조회용

package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoursePendingRes {
    private Long userId;
    private Long courseId; // 강의 ID
    private String courseCode;
    private String title; // 강의명
    private String classroom; // 강의실
    private String type;
    private Integer grade;
    private String time;
    private Long credit;
    private int maxStd;
    private String professorName;
    private String deptName;


}
