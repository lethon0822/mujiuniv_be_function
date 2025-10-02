package com.green.muziuniv_be_notuser.app.professor.attendance.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceReq {
    private String attendDate; // yyyy-MM-dd
    private Long enrollmentId;
    private String status; // 출석, 지각, 결석 등
    private String note;
}
