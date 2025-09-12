package com.green.muziuniv_be_notuser.entity.attendance;

import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance {

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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // INSERT 직전 실행 → 생성/수정 시간 자동 세팅
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // UPDATE 직전 실행 → 수정 시간 갱신
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
