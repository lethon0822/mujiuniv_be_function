package com.green.muziuniv_be_notuser.app.student.grade.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMyCurrentGradeRes {
    private Long enrollmentId; // 금학기 강의평가 req용으로 쓰기 위함

    private Long courseId;
    private String type;
    private Long userId;
    private String professorName;
    private String courseCode;
    private String title;
    private int credit;

    private String rank;
    private Integer midScore;
    private Integer finScore;
    private Integer attendanceScore;
    private Integer otherScore;
    private Double point;

    // 강의 평가를 했다면 진짜 성적을 알려주고, 강의 평가를 안 했다면 가려서 처리
    private Integer evScore;

}
