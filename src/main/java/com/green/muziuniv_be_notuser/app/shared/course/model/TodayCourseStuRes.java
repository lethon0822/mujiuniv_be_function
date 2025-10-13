package com.green.muziuniv_be_notuser.app.shared.course.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodayCourseStuRes {
    private long courseId;
    private String title;
    private long userId;
    private String classroom;
    private String time;
    private String type;
    private String userName;
}
