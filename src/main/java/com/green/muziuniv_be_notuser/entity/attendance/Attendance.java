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

    // 최초 생성 시간 (INSERT 시 한 번만 값 세팅, 이후 변경 X)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정 시간 (UPDATE 시마다 갱신됨)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // INSERT 직전 실행
    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // null 방지
        }
        this.updatedAt = LocalDateTime.now();
    }

    // UPDATE 직전 실행
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
