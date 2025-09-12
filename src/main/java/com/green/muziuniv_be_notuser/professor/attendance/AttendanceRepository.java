package com.green.muziuniv_be_notuser.professor.attendance;

import com.green.muziuniv_be_notuser.entity.attendance.Attendance;
import com.green.muziuniv_be_notuser.entity.attendance.AttendanceIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceIds> {
}

