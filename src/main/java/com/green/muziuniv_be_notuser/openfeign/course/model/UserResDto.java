package com.green.muziuniv_be_notuser.openfeign.course.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private Long userId; // 학생 ID
    private String loginId;
    private String userName; // 이름
    private Integer gradeYear; // 학년
    private String deptName; // 학과명
}
