package com.green.muziuniv_be_notuser.entity.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceIds> {
}

