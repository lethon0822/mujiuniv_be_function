package com.green.muziuniv_be_notuser.professor.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceRes {
    private String attendDate;
    private Long enrollmentId;
    private String status;
    private String note;
}
