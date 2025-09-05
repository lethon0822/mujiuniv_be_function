package com.green.muziuniv_be_notuser.entity.schedule;


import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Schedule extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semesterId;

    @Column(nullable = false, length = 20)
    private String schedule_type;

    @Embedded // 연결
    private TimeSetting timeSetting;

    @Column(nullable = false)
    private String description;


}
