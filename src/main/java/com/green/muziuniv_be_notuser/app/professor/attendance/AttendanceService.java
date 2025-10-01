
package com.green.muziuniv_be_notuser.app.professor.attendance;

import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceSummaryRes;
import com.green.muziuniv_be_notuser.entity.attendance.Attendance;
import com.green.muziuniv_be_notuser.entity.attendance.AttendanceIds;
import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceReq;
import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceRes saveAttendance(AttendanceReq req) {
//        // Enrollment 확인 (없으면 예외 발생)
        var enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        // 복합키 생성
        AttendanceIds ids = new AttendanceIds();
        ids.setAttendDate(LocalDate.parse(req.getAttendDate()));
        ids.setEnrollmentId(req.getEnrollmentId());

        // 엔티티 생성
        Attendance attendance = new Attendance();
        attendance.setAttendanceIds(ids);
        attendance.setEnrollment(enrollment);
        attendance.setStatus(req.getStatus());
        attendance.setNote(req.getNote());

        // 저장
        Attendance saved = attendanceRepository.save(attendance);

        // 응답 DTO 생성
        return new AttendanceRes(
                saved.getAttendanceIds().getAttendDate().toString(),
                saved.getAttendanceIds().getEnrollmentId(),
                saved.getStatus(),
                saved.getNote()
        );
    }
    public AttendanceSummaryRes getAttendanceSummary(Long enrollmentId) {
        int totalWeeks = 15; // 한 학기 주차 수
        int maxScore = 20;   // 출결점수 만점

        int attended = attendanceRepository.countByEnrollment_EnrollmentIdAndStatus(enrollmentId, "출석");
        int late = attendanceRepository.countByEnrollment_EnrollmentIdAndStatus(enrollmentId, "지각");
        int excused = attendanceRepository.countByEnrollment_EnrollmentIdAndStatus(enrollmentId, "공결");

        // 지각 3회 = 결석 1회
        int absent = (totalWeeks - attended - excused) + (late / 3);

        double attendanceScore = ((double) attended / totalWeeks) * maxScore;

        return new AttendanceSummaryRes(
                attended,
                absent,
                Math.round(attendanceScore * 10) / 10.0
        );
    }
}
