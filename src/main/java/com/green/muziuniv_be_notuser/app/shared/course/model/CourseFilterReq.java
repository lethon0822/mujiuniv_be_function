package com.green.muziuniv_be_notuser.app.shared.course.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseFilterReq {
    private Integer year;
    private Integer semester;
    private String type;
    private Integer grade;
    private String keyword;

    // 학과 필터 처리를 위해 필요
    private Long deptId;
    private List<Long> professorIds; //백엔드 내부 req
}
