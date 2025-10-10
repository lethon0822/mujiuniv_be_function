package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoursePendingReq {
    private Long sid;
    private String type;
    private String keyword;
}
