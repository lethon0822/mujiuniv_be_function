package com.green.muziuniv_be_notuser.professor.attendance;

import com.green.muziuniv_be_notuser.professor.attendance.model.AttendanceReq;
import com.green.muziuniv_be_notuser.professor.attendance.model.AttendanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/professor/course")
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
