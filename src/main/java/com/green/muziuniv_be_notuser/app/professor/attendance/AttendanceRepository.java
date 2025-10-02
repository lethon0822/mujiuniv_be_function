package com.green.muziuniv_be_notuser.app.professor.attendance;

import com.green.muziuniv_be_notuser.entity.attendance.Attendance;
import com.green.muziuniv_be_notuser.entity.attendance.AttendanceIds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceIds> {

    // 특정 수강(enrollmentId)의 전체 출결 수
    int countByEnrollment_EnrollmentId(Long enrollmentId);

    // 특정 수강(enrollmentId) + 상태(status)로 출결 카운트
    int countByEnrollment_EnrollmentIdAndStatus(Long enrollmentId, String status);

    // 강의 + 날짜별 출결 조회용
    List<Attendance> findByEnrollment_Course_CourseIdAndAttendanceIds_AttendDate(Long courseId, LocalDate attendDate);
}
