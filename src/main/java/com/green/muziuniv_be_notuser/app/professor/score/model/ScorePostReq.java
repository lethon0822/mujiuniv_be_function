package com.green.muziuniv_be_notuser.app.professor.score.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScorePostReq {
    private Long enrollmentId; // 수강신청 ID (어떤 학생이 어떤 강의를 듣는지 연결)

    private int midScore; // 중간고사 점수
    private int finScore; // 기말고사 점수
    private int attendanceScore; // 출결 점수
    private int otherScore; // 기타 점수 (과제, 발표, 프로젝트 등)

}
