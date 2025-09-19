package com.green.muziuniv_be_notuser.openfeign.course.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResDto {
    private Long userId; // 학생 ID
    private String loginId;
    private String userName; // 이름
    private int gradeYear; // 학년
    private String deptName; // 학과명
}
