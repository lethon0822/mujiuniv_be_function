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
    private Long scoreId; // 성적 ID (pk)
    private Long enrollmentId; // 수강신청 ID (fk)
    private String rank; // 등급 (A+, A, B+, ...)
//    private int grade; // 학점 값 (4=최고, 0=F)
}
