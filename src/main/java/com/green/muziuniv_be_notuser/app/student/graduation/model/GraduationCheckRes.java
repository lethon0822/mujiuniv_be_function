package com.green.muziuniv_be_notuser.app.student.graduation.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraduationCheckRes {

    // 취득 학점
    private int majorRequiredCredits; // 전공 필수
    private int majorElectiveCredits; // 전공 선택
    private int generalRequiredCredits; // 교양 필수
    private int generalElectiveCredits; // 교양 선택

    // 부족 학점
    private int shortageMajorRequiredCredits; // 전공 필수
    private int shortageMajorElectiveCredits; // 전공 선택
    private int shortageGeneralRequiredCredits; // 교양 필수
    private int shortageGeneralElectiveCredits; // 교양 선택

    // 총 취득 학점
    private int totalCompletedCredits;
    // 총 부족 학점
    private int totalShortageCredits;

    // 졸업 가능 여부
    private String graduationResult;

    // 안내 메세지
    private String details;

}
