package com.green.muziuniv_be_notuser.app.professor.attendance;

import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceSummaryRes;
import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceReq;
import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceRes;
import com.green.muziuniv_be_notuser.entity.attendance.Attendance;
import com.green.muziuniv_be_notuser.entity.attendance.AttendanceIds;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;

    /* 출결 저장 (INSERT or UPDATE) */
    public AttendanceRes saveAttendance(AttendanceReq req) {
        var enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        //  프론트에서 오는 날짜에 T00:00:00 방지
        String dateStr = req.getAttendDate().split("T")[0];
        LocalDate date = LocalDate.parse(dateStr);

        AttendanceIds ids = new AttendanceIds();
        ids.setAttendDate(date);
        ids.setEnrollmentId(req.getEnrollmentId());

        //  기존 출결 조회
        Attendance attendance = attendanceRepository.findById(ids).orElse(null);

        //  없으면 새로 INSERT
        if (attendance == null) {
            attendance = new Attendance();
            attendance.setAttendanceIds(ids);
            attendance.setEnrollment(enrollment);
            attendance.setCreatedAt(LocalDateTime.now()); // INSERT 때 필수
        }

        //  항상 갱신
        attendance.setStatus(req.getStatus());
        attendance.setNote(req.getNote());
        attendance.setUpdatedAt(LocalDateTime.now());

        Attendance saved = attendanceRepository.save(attendance);

        return new AttendanceRes(
                saved.getAttendanceIds().getAttendDate().toString(),
                saved.getAttendanceIds().getEnrollmentId(),
                saved.getStatus(),
                saved.getNote()
        );
    }

    /*  출결 요약 조회 (성적 계산용) */
    public AttendanceSummaryRes getAttendanceSummary(Long enrollmentId) {
        int totalDays = 50; // ✅ 수업일수 고정
        int attended = attendanceRepository.countByEnrollment_EnrollmentIdAndStatus(enrollmentId, "출석");
        int absent = totalDays - attended; // ✅ 자동 계산

        double attendanceEval;
        if (absent <= 5) attendanceEval = 100;
        else if (absent <= 9) attendanceEval = 90;
        else if (absent <= 13) attendanceEval = 80;
        else if (absent <= 17) attendanceEval = 70;
        else if (absent <= 21) attendanceEval = 60;
        else if (absent <= 25) attendanceEval = 50;
        else attendanceEval = 0;

        return new AttendanceSummaryRes(attended, absent, attendanceEval);
    }

    /* ✅ 강의별 특정 날짜 출결 조회 */
    public List<AttendanceRes> getAttendanceByDate(Long courseId, String attendDate) {
        LocalDate date = LocalDate.parse(attendDate); // "2025-10-02" → LocalDate 변환

        List<Attendance> list = attendanceRepository
                .findByEnrollment_Course_CourseIdAndAttendanceIds_AttendDate(courseId, date);

        return list.stream()
                .map(att -> new AttendanceRes(
                        att.getAttendanceIds().getAttendDate().toString(),
                        att.getAttendanceIds().getEnrollmentId(),
                        att.getStatus(),
                        att.getNote()
                ))
                .collect(Collectors.toList());
    }
}
