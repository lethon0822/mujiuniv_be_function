package com.green.muziuniv_be_notuser.app.staff.approval.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalAppGetRes {
    private Long appId;
    private Long userId;
    private String status;        // 처리중 / 승인 / 거부
    private String reason;
    private String createdAt;

    private String scheduleType;  // 신청 일정 유형
    private Integer year;
    private Integer semester;

    // user-service 에서 채워줄 값
    private String userName;
    private String deptName;


}
