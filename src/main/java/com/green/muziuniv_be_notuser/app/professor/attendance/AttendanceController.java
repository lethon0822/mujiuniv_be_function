package com.green.muziuniv_be_notuser.app.professor.attendance;

import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceReq;
import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    // 강의별 특정 날짜 출결 조회
    @GetMapping("/check/{courseId}")
    public ResponseEntity<List<AttendanceRes>> getAttendanceByDate(
            @PathVariable Long courseId,
            @RequestParam String attendDate
    ) {
        List<AttendanceRes> result = attendanceService.getAttendanceByDate(courseId, attendDate);
        return ResponseEntity.ok(result);
    }
}