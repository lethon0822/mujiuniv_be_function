package com.green.muziuniv_be_notuser.professor.attendance;

import com.green.muziuniv_be_notuser.entity.attendance.Attendance;
import com.green.muziuniv_be_notuser.entity.attendance.AttendanceIds;
import com.green.muziuniv_be_notuser.professor.attendance.model.AttendanceReq;
import com.green.muziuniv_be_notuser.professor.attendance.model.AttendanceRes;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.student.enrollment.EnrollmentRepository;


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
//        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
//                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        // 복합키 생성
        AttendanceIds ids = new AttendanceIds();
        ids.setAttendDate(LocalDate.parse(req.getAttendDate()));
        ids.setEnrollmentId(req.getEnrollmentId());

        // 엔티티 생성
        Attendance attendance = new Attendance();
        attendance.setAttendanceIds(ids);
//        attendance.setEnrollment(enrollment);
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
}