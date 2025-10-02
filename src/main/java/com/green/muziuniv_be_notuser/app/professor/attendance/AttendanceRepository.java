package com.green.muziuniv_be_notuser.app.professor.attendance;

import com.green.muziuniv_be_notuser.entity.attendance.Attendance;
import com.green.muziuniv_be_notuser.entity.attendance.AttendanceIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceIds> {

    // 특정 수강(enrollmentId) + 상태(status)로 출결 카운트
    int countByEnrollment_EnrollmentIdAndStatus(Long enrollmentId, String status);
}