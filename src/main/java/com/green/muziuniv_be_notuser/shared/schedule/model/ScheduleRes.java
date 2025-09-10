package com.green.muziuniv_be_notuser.shared.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRes {
    private Integer scheduleId;
    private Integer semesterId;
    private String scheduleType;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String description;
    private LocalDateTime createdAt;
}