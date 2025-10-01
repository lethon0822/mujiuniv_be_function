package com.green.muziuniv_be_notuser.app.professor.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceSummaryRes {
    private int attended;   // 출석일수
    private int absent;     // 결석일수
    private double attendanceEval;   // 출결점수
}
