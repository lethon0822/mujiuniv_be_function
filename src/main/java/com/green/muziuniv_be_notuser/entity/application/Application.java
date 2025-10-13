package com.green.muziuniv_be_notuser.entity.application;

import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.UserId;
import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "application",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"schedule_id", "user_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Application extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long appId;

    @Embedded
    private UserId userId;

    @ManyToOne
    @JoinColumn(
            name = "schedule_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_application_schedule")
    )
    private Schedule schedule;

    @Column(name = "schedule_type", nullable = false, length = 20)
    private String scheduleType;   // 예: "휴학신청", "복학신청", "휴직신청", "복직신청"

    @Column(nullable = false)
    private String status = "처리중";

    @Column(length = 255)
    private String reason;
}
