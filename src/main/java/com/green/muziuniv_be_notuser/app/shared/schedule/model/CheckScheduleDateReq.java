package com.green.muziuniv_be_notuser.app.shared.schedule.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CheckScheduleDateReq {
    private Long semesterId;
    private String type;
}
