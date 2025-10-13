package com.green.muziuniv_be_notuser.app.professor.score.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScorePutReq {
    private Long enrollmentId; // 수강신청 Id

    private int midScore; // 중간고사 점수
    private int finScore; // 기말고사 점수
    private int attendanceScore; // 출결 점수
    private int otherScore; // 기타 점수
    private int grade; // 학년
    private String rank; // 등급
}
