package com.green.muziuniv_be_notuser.entity.application;

import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.UserId;
import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import jakarta.persistence.*;

@Entity
@Table(
        name = "application",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "schedule_id", "user_id"
                        }
                )
        }

)
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
            foreignKey = @ForeignKey(name = "fk_application_schedule") // ✅ FK 이름 고정
    )
    private Schedule schedule;

    @Column(nullable = false)
    private String status = "처리중";

    private String reason;



}
