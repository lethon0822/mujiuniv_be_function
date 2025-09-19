package com.green.muziuniv_be_notuser.app.shared.courseEvaluation.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseEvaluationReq {
    private int enrollmentId;
    private String review;
    private int evScore;
}
