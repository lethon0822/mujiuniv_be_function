package com.green.muziuniv_be_notuser.entity.schedule;


import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.application.Application;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"semester_id", "schedule_type"})
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Schedule extends CreatedAt {

    @OneToMany(
            mappedBy = "schedule",                 // Application의 schedule 필드와 매핑
            cascade = CascadeType.REMOVE,          // Schedule 삭제 시 Application 같이 삭제
            orphanRemoval = true                   // 고아 객체 자동 삭제
    )
    private List<Application> applications = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "schedule_type", nullable = false, length = 20)
    private String scheduleType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDatetime", column = @Column(name = "start_datetime", nullable = false)),
            @AttributeOverride(name = "endDatetime",   column = @Column(name = "end_datetime",   nullable = false))
    })
    private TimeSetting timeSetting;

    @Column(length = 255) // 길이 제한 추천
    private String description;

    // === 도메인 변경 메서드(업데이트용) ===
    public void change(String scheduleType, TimeSetting timeSetting, String description) {
        this.scheduleType = scheduleType;
        this.timeSetting = timeSetting;
        this.description = description;
    }
}
