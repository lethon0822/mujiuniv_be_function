package com.green.muziuniv_be_notuser.entity.schedule;


import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule") // 명시 추천
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Schedule extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(nullable = false, length = 20)
    private String scheduleType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDatetime", column = @Column(name = "start_datetime", nullable = false)),
            @AttributeOverride(name = "endDatetime",   column = @Column(name = "end_datetime",   nullable = false))
    })
    private TimeSetting timeSetting;

    @Column(nullable = false, length = 255) // 길이 제한 추천
    private String description;

    // === 도메인 변경 메서드(업데이트용) ===
    public void change(String scheduleType, TimeSetting timeSetting, String description) {
        this.scheduleType = scheduleType;
        this.timeSetting = timeSetting;
        this.description = description;
    }
}
