package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApprovalPatchReq {

    private Long appId;
    private String status;   // 승인 or 거부
    private Long userId;
    private String scheduleType; // 신청 종류: 휴학신청 / 복학신청 / 휴직신청 / 복직신청

}
