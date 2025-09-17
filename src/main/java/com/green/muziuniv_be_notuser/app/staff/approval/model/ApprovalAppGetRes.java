package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalAppGetRes {
    private int appId;
    private String createdAt;
    private String status;
    private String reason;
    private Long userId;
    private String userName;
    private String deptName;
    private String scheduleType;
    private int year;
    private int semester;
}
