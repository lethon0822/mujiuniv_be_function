package com.green.muziuniv_be_notuser.app.student.grade.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllPermanentGradeReq {
    private Integer semesterId;
    private Integer semester;
    private Integer grade;
}
