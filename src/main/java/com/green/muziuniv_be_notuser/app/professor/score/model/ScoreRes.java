package com.green.muziuniv_be_notuser.app.professor.score.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRes {
    private Long scoreId;          // 성적 ID (PK)
    private Long enrollmentId;     // 수강 신청 ID (FK)
    private String rank;           // 등급 (A+, A, B+, ...)
    private int gradeYear;         // 학년 (user-service에서 가져옴)

    private int midScore;          // 중간고사 점수
    private int finScore;          // 기말고사 점수
    private int attendanceScore;   // 출결 점수
    private int otherScore;        // 기타 평가 점수

    private double total;          // 총점 (평균 계산 결과)
    private double gpa;            // 평점 (4.5 만점 기준)

    private int attendanceDays;    // ✅ 출석일수
    private int absentDays;          // ✅ 결석일수
}