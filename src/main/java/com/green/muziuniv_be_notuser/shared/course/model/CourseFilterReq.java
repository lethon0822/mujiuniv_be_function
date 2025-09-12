package com.green.muziuniv_be_notuser.shared.course.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFilterReq {
    private Integer year;
    private Integer semester;
    private String type;
    private Integer grade;
    private String keyword;
}
