package com.green.muziuniv_be_notuser.shared.schedule.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ScheduleCreateReq {
    @NotNull
    private Integer semesterId;
    @NotBlank
    @Size(max = 20)
    private String scheduleType;
    @NotNull
    private LocalDateTime startDatetime;
    @NotNull
    private LocalDateTime endDatetime;
    @NotBlank @Size(max = 255)
    private String description;
}
