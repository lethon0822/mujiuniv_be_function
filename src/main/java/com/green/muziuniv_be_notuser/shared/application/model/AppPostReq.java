package com.green.muziuniv_be_notuser.shared.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppPostReq {
    private int userId;
    private int scheduleId;
    private String reason;
}
