package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApprovalAppGetRes {

    private Long appId;           // 신청 ID
    private Long userId;          // 신청자 ID
    private String userName;      // 신청자 이름 (UserClient에서 매핑)
    private String deptName;      // 학과명 (UserClient에서 매핑)
    private String status;        // 신청 상태 (처리중 / 승인 / 거부)
    private String reason;        // 사유
    private LocalDateTime createdAt; // 신청일시

    private String scheduleType;  // DB의 실제 일정유형 (예: 휴직신청)
    private Integer year;         // 학년도
    private Integer semester;     // 학기 (1 / 2)
}
