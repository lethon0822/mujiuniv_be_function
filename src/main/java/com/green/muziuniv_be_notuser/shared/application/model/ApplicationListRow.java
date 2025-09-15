package com.green.muziuniv_be_notuser.shared.application.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationListRow {
    private Long appId;
    private String status;          // '처리중' | '승인' | '거부'
    private String reason;
    private LocalDateTime submittedAt;

    private String scheduleType;    // '휴학신청' | '복학신청'
    private LocalDateTime scheduleStart;
    private LocalDateTime scheduleEnd;

    private Integer year;           // YEAR
    private String semester;        // '1' | '2'

    private String deptName;   // ✅ 추가
}