package com.green.muziuniv_be_notuser.shared.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppGetRes {
    private int userId;
    private int scheduleId;
    private String status;
    private String reason;
    private String createdAt;
}
