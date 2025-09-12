package com.green.muziuniv_be_notuser.entity.attendance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceReq {
    private String attendDate; // yyyy-MM-dd
    private Integer enrollmentId;
    private String status; // 출석, 지각, 결석 등
    private String note;
}
