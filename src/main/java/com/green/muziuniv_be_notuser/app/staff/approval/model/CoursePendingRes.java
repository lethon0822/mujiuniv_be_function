// 조회용

package com.green.muziuniv_be_notuser.app.staff.approval.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoursePendingRes {
    private Long courseId; // 강의 ID
    private String title; // 강의명
    private String classroom; // 강의실
    private String status; // 상태 (처리중/승인)
    private String courseCode;
    private String time;
    private Long credit;
    private int maxStd;
    private String type;
    private String professorName;
    private String deptName;
    private Integer grade;
    @JsonIgnore
    private Long userId;

}
