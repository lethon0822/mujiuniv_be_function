package com.green.muziuniv_be_notuser.app.shared.course.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodayCourseReq {
    private int sid;
    private long userId;
    private String role;
    private String time;
}
