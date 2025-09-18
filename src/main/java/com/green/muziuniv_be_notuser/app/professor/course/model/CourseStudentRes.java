package com.green.muziuniv_be_notuser.app.professor.course.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 강의별 학생 리스트 응답 DTO
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CourseStudentRes {
    private Long enrollmentId;     // 수강신청 PK
    private Long userId;           // 학생 PK (loginId 대신 UserId)
    private String loginId;        // 학생 로그인 ID
    private String userName;       // 학생 이름
    private int gradeYear;         // 학년
    private String departmentName; // 학과명
}