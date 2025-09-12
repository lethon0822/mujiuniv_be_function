package com.green.muziuniv_be_notuser.entity.attendance;

import com.green.muziuniv_be_notuser.entity.attendance.model.AttendanceReq;
import com.green.muziuniv_be_notuser.entity.attendance.model.AttendanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/professor/course")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    // 강의별 학생 출결 기입
    @PutMapping("/check")
    public ResponseEntity<AttendanceRes> checkAttendance(@RequestBody AttendanceReq attendanceReq) {
        AttendanceRes res = attendanceService.saveAttendance(attendanceReq);
        return ResponseEntity.ok(res);
    }
}
