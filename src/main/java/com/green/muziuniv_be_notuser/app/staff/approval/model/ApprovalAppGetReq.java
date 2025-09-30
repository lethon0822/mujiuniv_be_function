package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalAppGetReq {
    private int year;
    private int semester;
    private String scheduleType;
}
