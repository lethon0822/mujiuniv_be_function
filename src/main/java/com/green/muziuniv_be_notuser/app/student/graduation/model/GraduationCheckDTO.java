package com.green.muziuniv_be_notuser.app.student.graduation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GraduationCheckDTO {
    // 이수구분 유형별 각각 총 몇 학점을 취득했는가?
    private String type;
    private Long totalCredits;
}
