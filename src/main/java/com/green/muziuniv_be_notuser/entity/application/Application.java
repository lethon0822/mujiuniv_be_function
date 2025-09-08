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
    private int appId;

    @Embedded
    private UserId userId;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private String status = "처리중";

    private String reason;



}
