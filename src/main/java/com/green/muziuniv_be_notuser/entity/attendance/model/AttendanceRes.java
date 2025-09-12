package com.green.muziuniv_be_notuser.entity.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceRes {
    private String attendDate;
    private Integer enrollmentId;
    private String status;
    private String note;
}
