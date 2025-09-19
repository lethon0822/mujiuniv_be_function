package com.green.muziuniv_be_notuser.app.professor.coursemanage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStudentGetRes {
    private int enrollmentId;
    private int userId;

    private String userName;   // ProGetRes에서 가져옴
    private String deptName;   // ProGetRes에서 가져옴
}
