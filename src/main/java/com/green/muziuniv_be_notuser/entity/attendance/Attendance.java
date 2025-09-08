package com.green.muziuniv_be_notuser.entity.attendance;

import com.green.muziuniv_be_notuser.entity.UpdatedAt;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import jakarta.persistence.*;



@Entity
public class Attendance extends UpdatedAt {

    @EmbeddedId
    private AttendanceIds attendanceIds;

    @MapsId("enrollmentId")
    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(length = 50, nullable = false)
    private String status = "결석";

    @Column(length = 100)
    private String note;
}
