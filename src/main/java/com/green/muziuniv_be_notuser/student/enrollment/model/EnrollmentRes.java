package com.green.muziuniv_be_notuser.student.enrollment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRes {
    private String status; //상태
    private int courseId; //과목 아이디
    private String courseCode; //과목 코드
    private String title; //강의 이름
    private String classroom; //강의실
    private String type; //이수 구분
    private String professorName; //담당 교수
    private int grade; //수강 학년
    private int year; //연도
    private int semester; // 학기
    private String time; // 강의시간
    private int credit; // 학점
    private int maxStd; // 정원
    private int remStd; //잔여 인원
    private String deptName; // 개설학과
}
