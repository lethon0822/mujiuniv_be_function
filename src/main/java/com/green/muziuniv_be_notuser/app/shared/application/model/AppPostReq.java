package com.green.muziuniv_be_notuser.app.shared.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppPostReq {
    private Long userId;
    private int scheduleId;
    private String reason;

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
